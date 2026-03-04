package com.kshitiz.capsule.utils

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import com.kshitiz.capsule.ui.theme.*
import androidx.compose.runtime.Immutable

/**
 * Manages System UI (Status Bar & Navigation Bar) appearance.
 * Handles both the initial Activity setup and reactive Compose state changes.
 */
 @Immutable
class SystemUIHelper(private val activity: Activity) {

    fun init() {
        val currentNightMode = activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isSystemDark = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        applyThemeAppearance(isAppDark = isSystemDark)
    }

    @Composable
    fun SystemBarsThemeEffect(mode: ThemeData) {
        val systemIsDark = isSystemInDarkTheme()

        val isEffectiveDark = when (mode) {
            ThemeData.DARK -> true
            ThemeData.LIGHT -> false
            ThemeData.SYSTEM -> systemIsDark
        }

        LaunchedEffect(isEffectiveDark) {
            applyThemeAppearance(isAppDark = isEffectiveDark)
        }
    }

        fun applyThemeAppearance(isAppDark: Boolean) { // REMOVED 'private'

        // 1. Edge-to-Edge (Safe to call immediately)
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)

        // 2. Colors
        val navBarColor = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            Color.argb(1, 0, 0, 0)
        } else {
            Color.TRANSPARENT
        }

        activity.window.statusBarColor = Color.TRANSPARENT
        activity.window.navigationBarColor = navBarColor
        
        // --- FIX 1: Disable System Contrast Enforcement ---
        // This prevents the system from adding a translucent scrim behind the nav bar
        // to ensure contrast. We disable it because we want full transparency.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.window.isNavigationBarContrastEnforced = false
        }

        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // 3. Icons Logic
        val setLightIcons = !isAppDark // Light Status Bar = Dark Icons
        val decorView = activity.window.decorView

        // --- FIX 2: Align Timing with com.my.water ---
        // Instead of checking if attached, we consistently post the update.
        // This slight delay helps MIUI apply the flags correctly without artifacts.
        decorView.post {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val insetsController = activity.window.insetsController
                if (insetsController != null) {
                    var appearance = 0
                    if (setLightIcons) {
                        appearance = appearance or WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        appearance = appearance or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    }
                    insetsController.setSystemBarsAppearance(
                        appearance,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or
                                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                    )
                }
            } else {
                @Suppress("DEPRECATION")
                var flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)

                if (setLightIcons) {
                    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    }
                }
                decorView.systemUiVisibility = flags
            }
        }
    }
}
