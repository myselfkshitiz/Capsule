package com.kshitiz.capsule.service

import android.accessibilityservice.AccessibilityService
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.kshitiz.capsule.ui.theme.TimeCapsuleTheme
import com.kshitiz.capsule.utils.PrefsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PillAccessibilityService : AccessibilityService(), LifecycleOwner, SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    private lateinit var wm: WindowManager
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var composeView: ComposeView
    private lateinit var prefs: PrefsManager

    private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "x_offset" || key == "y_offset") {
            updatePillPosition()
        }
    }

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        
        prefs = PrefsManager(this)
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
    }

    override fun onServiceConnected() {
        prefs.registerListener(prefListener)

        composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@PillAccessibilityService)
            setViewTreeSavedStateRegistryOwner(this@PillAccessibilityService)
            
            setContent {
                TimeCapsuleTheme {
                    val isDark = isSystemInDarkTheme()
                    val colorScheme = MaterialTheme.colorScheme
                    val haptic = LocalHapticFeedback.current
                    
                    var currentTime by remember { mutableStateOf(getFormattedTime()) }
                    var formatTrigger by remember { mutableIntStateOf(0) }
                    var styleTrigger by remember { mutableIntStateOf(0) }
                    
                    var pWidth by remember { mutableIntStateOf(prefs.pillWidth) }
                    var pHeight by remember { mutableIntStateOf(prefs.pillHeight) }
                    var tSize by remember { mutableIntStateOf(prefs.textSize) }

                    var cTL by remember { mutableIntStateOf(prefs.cornerTL) }
                    var cTR by remember { mutableIntStateOf(prefs.cornerTR) }
                    var cBL by remember { mutableIntStateOf(prefs.cornerBL) }
                    var cBR by remember { mutableIntStateOf(prefs.cornerBR) }

                    var isHidden by remember { mutableStateOf(false) }

                    val pillAlpha by animateFloatAsState(
                        targetValue = if (isHidden) 0f else 1f,
                        animationSpec = tween(durationMillis = 300),
                        label = "PillAlphaFade"
                    )

                    DisposableEffect(Unit) {
                        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                            when {
                                key in listOf("is_24_hour", "is_am_pm", "show_seconds") -> formatTrigger++
                                key?.contains("pill_") == true || key == "text_size" -> {
                                    pWidth = prefs.pillWidth
                                    pHeight = prefs.pillHeight
                                    tSize = prefs.textSize
                                }
                                key?.startsWith("corner_") == true -> {
                                    cTL = prefs.cornerTL
                                    cTR = prefs.cornerTR
                                    cBL = prefs.cornerBL
                                    cBR = prefs.cornerBR
                                }
                                key?.startsWith("bg_") == true || key?.startsWith("color_") == true || 
                                key?.startsWith("token_") == true || key?.startsWith("hex_") == true ||
                                key?.startsWith("text_") == true -> styleTrigger++
                            }
                        }
                        prefs.registerListener(listener)
                        onDispose { prefs.unregisterListener(listener) }
                    }

                    LaunchedEffect(prefs.showSeconds, formatTrigger) {
                        while (isActive) {
                            currentTime = getFormattedTime()
                            if (prefs.showSeconds) {
                                delay(1000L)
                            } else {
                                val delayMillis = 60000L - (System.currentTimeMillis() % 60000L)
                                delay(delayMillis + 100L)
                            }
                        }
                    }

                    val resolveColor = { type: String, token: String, lightHex: String, darkHex: String ->
                        if (type == "TOKEN") {
                            when (token) {
                                "Primary" -> colorScheme.primary
                                "OnPrimary" -> colorScheme.onPrimary
                                "Secondary" -> colorScheme.secondary
                                "OnSecondary" -> colorScheme.onSecondary
                                "Tertiary" -> colorScheme.tertiary
                                "Surface" -> colorScheme.surface
                                "OnSurface" -> colorScheme.onSurface
                                "SurfaceVariant" -> colorScheme.surfaceVariant
                                "Error" -> colorScheme.error
                                else -> colorScheme.primary
                            }
                        } else {
                            try {
                                val hexString = if (isDark) darkHex else lightHex
                                Color(android.graphics.Color.parseColor(hexString))
                            } catch (e: Exception) {
                                if (isDark) Color.White else Color.Black
                            }
                        }
                    }

                    val bgBrush = remember(styleTrigger, isDark) {
                        val solidColor = resolveColor(prefs.colorType, prefs.tokenSolid, prefs.hexSolidLight, prefs.hexSolidDark)
                        val startColor = resolveColor(prefs.colorType, prefs.tokenGradStart, prefs.hexGradStartLight, prefs.hexGradStartDark)
                        val endColor = resolveColor(prefs.colorType, prefs.tokenGradEnd, prefs.hexGradEndLight, prefs.hexGradEndDark)

                        when (prefs.bgMode) {
                            "GRADIENT_V" -> Brush.verticalGradient(listOf(startColor, endColor))
                            "GRADIENT_H" -> Brush.horizontalGradient(listOf(startColor, endColor))
                            else -> Brush.linearGradient(listOf(solidColor, solidColor))
                        }
                    }

                    val resolvedTextColor = remember(styleTrigger, isDark) {
                        resolveColor(prefs.textColorType, prefs.textToken, prefs.textHexLight, prefs.textHexDark)
                    }
                    
                    val resolvedFontWeight = remember(styleTrigger) {
                        when (prefs.textWeight) {
                            "BOLD" -> FontWeight.Bold
                            "MEDIUM" -> FontWeight.Medium
                            else -> FontWeight.Normal
                        }
                    }

                    val pillShape = RoundedCornerShape(
                        topStart = cTL.dp,
                        topEnd = cTR.dp,
                        bottomStart = cBL.dp,
                        bottomEnd = cBR.dp
                    )

                    val stylingModifier = if (prefs.fillMode == "OUTLINE") {
                        Modifier.border(
                            width = prefs.strokeWidth.dp, 
                            brush = bgBrush, 
                            shape = pillShape
                        )
                    } else {
                        Modifier.background(
                            brush = bgBrush, 
                            shape = pillShape
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(width = pWidth.dp, height = pHeight.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isHidden = !isHidden
                                    }
                                )
                            }
                            .alpha(pillAlpha)
                            .then(stylingModifier),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentTime,
                            color = resolvedTextColor,
                            fontSize = tSize.sp,
                            fontWeight = resolvedFontWeight,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Clip,
                            style = MaterialTheme.typography.labelLarge.copy(
                                shadow = if (prefs.textShadow) Shadow(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 6f
                                ) else null
                            )
                        )
                    }
                }
            }
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or 
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or 
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = prefs.xOffset
            y = prefs.yOffset
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }

        wm.addView(composeView, params)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    private fun updatePillPosition() {
        if (::params.isInitialized && ::composeView.isInitialized) {
            params.x = prefs.xOffset
            params.y = prefs.yOffset
            wm.updateViewLayout(composeView, params)
        }
    }

    private fun getFormattedTime(): String {
        val pattern = buildString {
            if (prefs.is24HourFormat) append("HH:mm") else append("h:mm")
            if (prefs.showSeconds) append(":ss")
            if (!prefs.is24HourFormat && prefs.isAmPmEnabled) append(" a")
        }
        return try {
            SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
        } catch (e: Exception) {
            "00:00"
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        prefs.unregisterListener(prefListener)
        if (::composeView.isInitialized) {
            wm.removeView(composeView)
        }
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
