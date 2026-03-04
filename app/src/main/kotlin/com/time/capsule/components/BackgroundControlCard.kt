package com.kshitiz.capsule.components

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundControlCard(
    fillMode: String,
    onFillModeChange: (String) -> Unit,
    strokeWidth: Int,
    onStrokeWidthChange: (Int) -> Unit,
    bgMode: String,
    onBgModeChange: (String) -> Unit,
    colorType: String,
    onColorTypeChange: (String) -> Unit,
    tokenSolid: String,
    onTokenSolidChange: (String) -> Unit,
    tokenGradStart: String,
    onTokenGradStartChange: (String) -> Unit,
    tokenGradEnd: String,
    onTokenGradEndChange: (String) -> Unit,
    hexSolidLight: String,
    onHexSolidLightChange: (String) -> Unit,
    hexSolidDark: String,
    onHexSolidDarkChange: (String) -> Unit,
    hexGradStartLight: String,
    onHexGradStartLightChange: (String) -> Unit,
    hexGradStartDark: String,
    onHexGradStartDarkChange: (String) -> Unit,
    hexGradEndLight: String,
    onHexGradEndLightChange: (String) -> Unit,
    hexGradEndDark: String,
    onHexGradEndDarkChange: (String) -> Unit
) {
    var showSolidHexDialog by remember { mutableStateOf(false) }
    var showGradStartHexDialog by remember { mutableStateOf(false) }
    var showGradEndHexDialog by remember { mutableStateOf(false) }

    val tokens = listOf("Primary", "Secondary", "Tertiary", "Surface", "SurfaceVariant", "Error", "OnPrimary", "OnSurface")
    var expandedSolid by remember { mutableStateOf(false) }
    var expandedStart by remember { mutableStateOf(false) }
    var expandedEnd by remember { mutableStateOf(false) }

    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Background Style",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = fillMode == "FILL",
                    onClick = { onFillModeChange("FILL") },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) { Text("Fill") }
                SegmentedButton(
                    selected = fillMode == "OUTLINE",
                    onClick = { onFillModeChange("OUTLINE") },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) { Text("Outline") }
            }

            AnimatedVisibility(
                visible = fillMode == "OUTLINE",
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Stroke Thickness: ${strokeWidth}dp",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Slider(
                        value = strokeWidth.toFloat(),
                        onValueChange = { onStrokeWidthChange(it.toInt()) },
                        valueRange = 1f..15f
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = bgMode == "SOLID",
                    onClick = { onBgModeChange("SOLID") },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                ) { Text("Solid") }
                SegmentedButton(
                    selected = bgMode == "GRADIENT_V",
                    onClick = { onBgModeChange("GRADIENT_V") },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                ) { Text("Grad-V") }
                SegmentedButton(
                    selected = bgMode == "GRADIENT_H",
                    onClick = { onBgModeChange("GRADIENT_H") },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                ) { Text("Grad-H") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = colorType == "TOKEN",
                    onClick = { onColorTypeChange("TOKEN") },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) { Text("M3 Token") }
                SegmentedButton(
                    selected = colorType == "CUSTOM",
                    onClick = { onColorTypeChange("CUSTOM") },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) { Text("Custom Hex") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = Pair(bgMode, colorType),
                label = "ColorSelectionAnimation"
            ) { (currentBgMode, currentColorType) ->
                Column {
                    if (currentBgMode == "SOLID") {
                        if (currentColorType == "TOKEN") {
                            TokenDropdown("Solid Color", tokenSolid, expandedSolid, { expandedSolid = it }, tokens, onTokenSolidChange)
                        } else {
                            ColorPreviewButton("Solid Hex Colors", hexSolidLight, hexSolidDark) { showSolidHexDialog = true }
                        }
                    } else {
                        if (currentColorType == "TOKEN") {
                            TokenDropdown("Gradient Start", tokenGradStart, expandedStart, { expandedStart = it }, tokens, onTokenGradStartChange)
                            Spacer(modifier = Modifier.height(8.dp))
                            TokenDropdown("Gradient End", tokenGradEnd, expandedEnd, { expandedEnd = it }, tokens, onTokenGradEndChange)
                        } else {
                            ColorPreviewButton("Grad Start Hex", hexGradStartLight, hexGradStartDark) { showGradStartHexDialog = true }
                            Spacer(modifier = Modifier.height(8.dp))
                            ColorPreviewButton("Grad End Hex", hexGradEndLight, hexGradEndDark) { showGradEndHexDialog = true }
                        }
                    }
                }
            }
        }
    }

    if (showSolidHexDialog) {
        HexColorDialog("Solid Background", hexSolidLight, hexSolidDark, { showSolidHexDialog = false }) { l, d ->
            onHexSolidLightChange(l)
            onHexSolidDarkChange(d)
            showSolidHexDialog = false
        }
    }
    if (showGradStartHexDialog) {
        HexColorDialog("Gradient Start", hexGradStartLight, hexGradStartDark, { showGradStartHexDialog = false }) { l, d ->
            onHexGradStartLightChange(l)
            onHexGradStartDarkChange(d)
            showGradStartHexDialog = false
        }
    }
    if (showGradEndHexDialog) {
        HexColorDialog("Gradient End", hexGradEndLight, hexGradEndDark, { showGradEndHexDialog = false }) { l, d ->
            onHexGradEndLightChange(l)
            onHexGradEndDarkChange(d)
            showGradEndHexDialog = false
        }
    }
}

@Composable
fun ColorPreviewButton(label: String, lightHex: String, darkHex: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = MaterialTheme.colorScheme.onSurface)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ColorBox(lightHex)
                ColorBox(darkHex)
            }
        }
    }
}

@Composable
private fun ColorBox(hex: String) {
    val color = try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { Color.Transparent }
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokenDropdown(
    label: String,
    selected: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    tokens: List<String>,
    onSelect: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
        ) {
            tokens.forEach { token ->
                DropdownMenuItem(
                    text = { Text(token) },
                    onClick = {
                        onSelect(token)
                        onExpandedChange(false)
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .clip(RoundedCornerShape(24.dp))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HexColorDialog(
    title: String,
    initialLight: String,
    initialDark: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    val parseColor = { hex: String ->
        try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { Color.Black }
    }

    var lightColor by remember { mutableStateOf(parseColor(initialLight)) }
    var darkColor by remember { mutableStateOf(parseColor(initialDark)) }
    var isDarkTab by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.animateContentSize()) {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = !isDarkTab,
                        onClick = { isDarkTab = false },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) { Text("Light Mode") }
                    SegmentedButton(
                        selected = isDarkTab,
                        onClick = { isDarkTab = true },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) { Text("Dark Mode") }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(
                    targetState = isDarkTab,
                    label = "HexColorTabAnimation"
                ) { isDark ->
                    if (isDark) {
                        NativeColorPicker(darkColor) { darkColor = it }
                    } else {
                        NativeColorPicker(lightColor) { lightColor = it }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(lightColor.toHexString(), darkColor.toHexString()) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun NativeColorPicker(color: Color, onColorChange: (Color) -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(color)
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))

        var hexText by remember(color) { mutableStateOf(color.toHexString()) }
        OutlinedTextField(
            value = hexText,
            onValueChange = {
                hexText = it
                if (it.matches(Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$"))) {
                    try { onColorChange(Color(android.graphics.Color.parseColor(it))) } catch (e: Exception) {}
                }
            },
            label = { Text("Hex Code") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        ColorSlider("R", color.red, { onColorChange(color.copy(red = it)) }, Color.Red)
        ColorSlider("G", color.green, { onColorChange(color.copy(green = it)) }, Color.Green)
        ColorSlider("B", color.blue, { onColorChange(color.copy(blue = it)) }, Color.Blue)
        ColorSlider("A", color.alpha, { onColorChange(color.copy(alpha = it)) }, Color.Gray)
    }
}

@Composable
private fun ColorSlider(label: String, value: Float, onValueChange: (Float) -> Unit, trackColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            modifier = Modifier.width(24.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..1f,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(thumbColor = trackColor, activeTrackColor = trackColor)
        )
    }
}

fun Color.toHexString(): String {
    val a = (this.alpha * 255).toInt()
    val r = (this.red * 255).toInt()
    val g = (this.green * 255).toInt()
    val b = (this.blue * 255).toInt()
    return String.format("#%02X%02X%02X%02X", a, r, g, b)
}
