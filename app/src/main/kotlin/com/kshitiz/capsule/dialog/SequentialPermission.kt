package com.kshitiz.capsule.dialog

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun PermissionFlowScreen(onAllPermissionsGranted: @Composable () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val sharedPrefs = remember { 
        context.getSharedPreferences("capsule_permissions", Context.MODE_PRIVATE) 
    }

    var isAccessibilityGranted by remember { mutableStateOf(checkAccessibility(context)) }
    var isBatteryOptimized by remember { mutableStateOf(checkBatteryOptimization(context)) }
    var hasSeenAutoStart by remember { mutableStateOf(sharedPrefs.getBoolean("seen_autostart", false)) }
    
    val isXiaomi = remember {
        val manufacturer = Build.MANUFACTURER.lowercase()
        manufacturer.contains("xiaomi") || manufacturer.contains("redmi") || manufacturer.contains("poco")
    }

    var showAccessibilityError by remember { mutableStateOf(false) }
    var showBatteryError by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isAccessibilityGranted = checkAccessibility(context)
                isBatteryOptimized = checkBatteryOptimization(context)
                
                if (isAccessibilityGranted) showAccessibilityError = false
                if (isBatteryOptimized) showBatteryError = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    when {
        !isAccessibilityGranted -> {
            GenericPermissionDialog(
                title = "Accessibility Service",
                description = "Time Capsule needs Accessibility Service enabled.",
                reasoning = "This allows us to draw the clock pill directly over your status bar system UI.",
                showError = showAccessibilityError,
                onConfirm = {
                    showAccessibilityError = false
                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                },
                onDismiss = { showAccessibilityError = true }
            )
        }
        !isBatteryOptimized -> {
            GenericPermissionDialog(
                title = "Battery Optimization",
                description = "We need to run in the background seamlessly.",
                reasoning = "Disabling battery optimization ensures your real-time clock doesn't freeze when the screen is off.",
                showError = showBatteryError,
                onConfirm = {
                    showBatteryError = false
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    context.startActivity(intent)
                },
                onDismiss = { showBatteryError = true }
            )
        }
        isXiaomi && !hasSeenAutoStart -> {
            GenericPermissionDialog(
                title = "Xiaomi Background Fix",
                description = "Your device strictly kills background apps.",
                reasoning = "To prevent the capsule from vanishing, please open settings, enable 'Auto-start', and set Battery Saver to 'No Restrictions'.",
                showError = false, 
                confirmText = "Open Settings",
                dismissText = "I've done this (Proceed)",
                onConfirm = {
                    sharedPrefs.edit().putBoolean("seen_autostart", true).apply()
                    hasSeenAutoStart = true
                    openXiaomiAutoStart(context)
                },
                onDismiss = { 
                    sharedPrefs.edit().putBoolean("seen_autostart", true).apply()
                    hasSeenAutoStart = true
                }
            )
        }
        else -> {
            onAllPermissionsGranted()
        }
    }
}

fun checkAccessibility(context: Context): Boolean {
    val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
    return enabledServices.any { it.resolveInfo.serviceInfo.packageName == context.packageName }
}

fun checkBatteryOptimization(context: Context): Boolean {
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return pm.isIgnoringBatteryOptimizations(context.packageName)
}

fun openXiaomiAutoStart(context: Context) {
    try {
        val intent = Intent().apply {
            component = android.content.ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
