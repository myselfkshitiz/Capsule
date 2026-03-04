package com.kshitiz.capsule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kshitiz.capsule.dialog.PermissionFlowScreen
import com.kshitiz.capsule.ui.screens.MainScreen
import com.kshitiz.capsule.ui.theme.TimeCapsuleTheme
import com.kshitiz.capsule.ui.theme.ThemeData
import com.kshitiz.capsule.utils.SystemUIHelper

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val systemUIHelper = SystemUIHelper(this)
        systemUIHelper.init()
        
        enableEdgeToEdge()

        setContent {
            val isDark = isSystemInDarkTheme()

            TimeCapsuleTheme(darkTheme = isDark) {
                systemUIHelper.SystemBarsThemeEffect(mode = ThemeData.SYSTEM)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PermissionFlowScreen {
                        MainScreen()
                    }
                }
            }
        }
    }
}
