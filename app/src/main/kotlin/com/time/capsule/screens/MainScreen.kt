package com.kshitiz.capsule.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kshitiz.capsule.components.*
import com.kshitiz.capsule.utils.PrefsManager
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val colorScheme = MaterialTheme.colorScheme
    
    
    var importTrigger by remember { mutableIntStateOf(0) }

    key(importTrigger) {
        val prefs = remember { PrefsManager(context) }

        
        var xOffset by remember { mutableIntStateOf(prefs.xOffset) }
        var yOffset by remember { mutableIntStateOf(prefs.yOffset) }
        var pillWidth by remember { mutableIntStateOf(prefs.pillWidth) }
        var pillHeight by remember { mutableIntStateOf(prefs.pillHeight) }
        var textSize by remember { mutableIntStateOf(prefs.textSize) }
        var keepRatio by remember { mutableStateOf(prefs.keepRatio) }
        var cornerTL by remember { mutableIntStateOf(prefs.cornerTL) }
        var cornerTR by remember { mutableIntStateOf(prefs.cornerTR) }
        var cornerBL by remember { mutableIntStateOf(prefs.cornerBL) }
        var cornerBR by remember { mutableIntStateOf(prefs.cornerBR) }

        var is24Hour by remember { mutableStateOf(prefs.is24HourFormat) }
        var isAmPm by remember { mutableStateOf(prefs.isAmPmEnabled) }
        var showSeconds by remember { mutableStateOf(prefs.showSeconds) }

        var fillMode by remember { mutableStateOf(prefs.fillMode) }
        var strokeWidth by remember { mutableIntStateOf(prefs.strokeWidth) }
        var bgMode by remember { mutableStateOf(prefs.bgMode) }
        var colorType by remember { mutableStateOf(prefs.colorType) }
        var tokenSolid by remember { mutableStateOf(prefs.tokenSolid) }
        var tokenGradStart by remember { mutableStateOf(prefs.tokenGradStart) }
        var tokenGradEnd by remember { mutableStateOf(prefs.tokenGradEnd) }
        var hexSolidLight by remember { mutableStateOf(prefs.hexSolidLight) }
        var hexSolidDark by remember { mutableStateOf(prefs.hexSolidDark) }
        var hexGradStartLight by remember { mutableStateOf(prefs.hexGradStartLight) }
        var hexGradStartDark by remember { mutableStateOf(prefs.hexGradStartDark) }
        var hexGradEndLight by remember { mutableStateOf(prefs.hexGradEndLight) }
        var hexGradEndDark by remember { mutableStateOf(prefs.hexGradEndDark) }

        var textColorType by remember { mutableStateOf(prefs.textColorType) }
        var textToken by remember { mutableStateOf(prefs.textToken) }
        var textWeight by remember { mutableStateOf(prefs.textWeight) }
        var textShadow by remember { mutableStateOf(prefs.textShadow) }
        var textHexLight by remember { mutableStateOf(prefs.textHexLight) }
        var textHexDark by remember { mutableStateOf(prefs.textHexDark) }

        val scaffoldBgColor = if (isDarkTheme) colorScheme.background else colorScheme.surfaceContainer
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        var showTextHexDialog by remember { mutableStateOf(false) }

        
        val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri ->
            uri?.let {
                val success = exportSettingsToFile(context, it)
                Toast.makeText(context, if (success) "Exported successfully!" else "Export failed", Toast.LENGTH_SHORT).show()
            }
        }

        val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                val success = importSettingsFromFile(context, it)
                if (success) {
                    importTrigger++ 
                    Toast.makeText(context, "Settings restored!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid or corrupted .capsule file", Toast.LENGTH_LONG).show()
                }
            }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = scaffoldBgColor,
            
            contentWindowInsets = WindowInsets.statusBars,
            topBar = { 
                BubbleTopBar(
                    scrollBehavior = scrollBehavior, 
                    onChipClick = { Toast.makeText(context, "Capsule v2.0.2", Toast.LENGTH_SHORT).show() }
                ) 
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                
                contentPadding = PaddingValues(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 40.dp,
                    top = 12.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                
                item(key = "header_status") { SectionHeader("STATUS") }
                item(key = "card_status") {
                    StatusCard(colorScheme)
                }

                
                item(key = "header_appearance") { SectionHeader("APPEARANCE") }
                item(key = "card_background") {
                    BackgroundControlCard(
                        fillMode = fillMode, onFillModeChange = { fillMode = it; prefs.fillMode = it },
                        strokeWidth = strokeWidth, onStrokeWidthChange = { strokeWidth = it; prefs.strokeWidth = it },
                        bgMode = bgMode, onBgModeChange = { bgMode = it; prefs.bgMode = it },
                        colorType = colorType, onColorTypeChange = { colorType = it; prefs.colorType = it },
                        tokenSolid = tokenSolid, onTokenSolidChange = { tokenSolid = it; prefs.tokenSolid = it },
                        tokenGradStart = tokenGradStart, onTokenGradStartChange = { tokenGradStart = it; prefs.tokenGradStart = it },
                        tokenGradEnd = tokenGradEnd, onTokenGradEndChange = { tokenGradEnd = it; prefs.tokenGradEnd = it },
                        hexSolidLight = hexSolidLight, onHexSolidLightChange = { hexSolidLight = it; prefs.hexSolidLight = it },
                        hexSolidDark = hexSolidDark, onHexSolidDarkChange = { hexSolidDark = it; prefs.hexSolidDark = it },
                        hexGradStartLight = hexGradStartLight, onHexGradStartLightChange = { hexGradStartLight = it; prefs.hexGradStartLight = it },
                        hexGradStartDark = hexGradStartDark, onHexGradStartDarkChange = { hexGradStartDark = it; prefs.hexGradStartDark = it },
                        hexGradEndLight = hexGradEndLight, onHexGradEndLightChange = { hexGradEndLight = it; prefs.hexGradEndLight = it },
                        hexGradEndDark = hexGradEndDark, onHexGradEndDarkChange = { hexGradEndDark = it; prefs.hexGradEndDark = it }
                    )
                }
                item(key = "card_text") {
                    TextControlCard(
                        colorType = textColorType, onColorTypeChange = { textColorType = it; prefs.textColorType = it },
                        textToken = textToken, onTextTokenChange = { textToken = it; prefs.textToken = it },
                        textWeight = textWeight, onTextWeightChange = { textWeight = it; prefs.textWeight = it },
                        textShadow = textShadow, onTextShadowChange = { textShadow = it; prefs.textShadow = it },
                        onEditHexClick = { showTextHexDialog = true },
                        textHexDark = textHexDark, textHexLight = textHexLight
                    )
                }

                
                item(key = "header_geometry") { SectionHeader("GEOMETRY") }
                item(key = "card_position") {
                    PositionControlCard(xOffset = xOffset, onXChange = { xOffset = it; prefs.xOffset = it }, yOffset = yOffset, onYChange = { yOffset = it; prefs.yOffset = it })
                }
                item(key = "card_size") {
                    PillSizeControlCard(
                        pillWidth = pillWidth, onWidthChange = { pillWidth = it; prefs.pillWidth = it },
                        pillHeight = pillHeight, onHeightChange = { pillHeight = it; prefs.pillHeight = it },
                        textSize = textSize, onTextSizeChange = { textSize = it; prefs.textSize = it },
                        keepRatio = keepRatio, onKeepRatioChange = { keepRatio = it; prefs.keepRatio = it }
                    )
                }
                item(key = "card_radius") {
                    CornerRadiusControlCard(
                        cornerTL = cornerTL, onCornerTLChange = { cornerTL = it; prefs.cornerTL = it },
                        cornerTR = cornerTR, onCornerTRChange = { cornerTR = it; prefs.cornerTR = it },
                        cornerBL = cornerBL, onCornerBLChange = { cornerBL = it; prefs.cornerBL = it },
                        cornerBR = cornerBR, onCornerBRChange = { cornerBR = it; prefs.cornerBR = it }
                    )
                }

                
                item(key = "header_format") { SectionHeader("CLOCK FORMAT") }
                item(key = "card_format") {
                    TimeFormatControlCard(
                        is24Hour = is24Hour, on24HourChange = { is24Hour = it; prefs.is24HourFormat = it },
                        isAmPm = isAmPm, onAmPmChange = { isAmPm = it; prefs.isAmPmEnabled = it },
                        showSeconds = showSeconds, onShowSecondsChange = { showSeconds = it; prefs.showSeconds = it }
                    )
                }

                
                item(key = "header_backup") { SectionHeader("BACKUP & RESTORE") }
                item(key = "card_backup") {
                    BackupCard(
                        colorScheme = colorScheme,
                        onImport = { importLauncher.launch(arrayOf("*/*")) },
                        onExport = { exportLauncher.launch("my_bubble.capsule") }
                    )
                }
            }
        }

        if (showTextHexDialog) {
            HexColorDialog("Text Hex Colors", textHexLight, textHexDark, { showTextHexDialog = false }) { l, d ->
                textHexLight = l; prefs.textHexLight = l
                textHexDark = d; prefs.textHexDark = d
                showTextHexDialog = false
            }
        }
    }
}



@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
private fun StatusCard(colorScheme: ColorScheme) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Service is Active", style = MaterialTheme.typography.labelLarge, color = colorScheme.primary)
            Text("The bubble is currently visible in your status bar.", style = MaterialTheme.typography.bodyMedium, color = colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun BackupCard(colorScheme: ColorScheme, onImport: () -> Unit, onExport: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "Save or load your configurations. Files use .capsule extension.", 
                style = MaterialTheme.typography.bodyMedium, 
                color = colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onImport,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                ) { Text("Import") }
                
                OutlinedButton(
                    onClick = onExport,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                ) { Text("Export") }
            }
        }
    }
}

fun exportSettingsToFile(context: Context, uri: Uri): Boolean {
    return try {
        val prefs = context.getSharedPreferences("bubble_prefs", Context.MODE_PRIVATE)
        val jsonObject = JSONObject()
        jsonObject.put("capsule_signature", "v1.0")
        prefs.all.forEach { (key, value) -> jsonObject.put(key, value) }
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(jsonObject.toString().toByteArray(Charsets.UTF_8))
        }
        true
    } catch (e: Exception) {
        false
    }
}

fun importSettingsFromFile(context: Context, uri: Uri): Boolean {
    return try {
        val jsonString = context.contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }
        if (jsonString.isNullOrEmpty()) return false
        val jsonObject = JSONObject(jsonString)
        if (!jsonObject.has("capsule_signature")) return false
        val editor = context.getSharedPreferences("bubble_prefs", Context.MODE_PRIVATE).edit()
        val keys = jsonObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            if (key == "capsule_signature") continue 
            when (val value = jsonObject.get(key)) {
                is Boolean -> editor.putBoolean(key, value)
                is Int -> editor.putInt(key, value)
                is String -> editor.putString(key, value)
                is Float -> editor.putFloat(key, value)
                is Long -> editor.putLong(key, value)
            }
        }
        editor.apply()
        true
    } catch (e: Exception) {
        false
    }
}
