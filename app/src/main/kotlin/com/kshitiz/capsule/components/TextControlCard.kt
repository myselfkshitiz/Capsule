package com.kshitiz.capsule.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextControlCard(
    colorType: String,
    onColorTypeChange: (String) -> Unit,
    textToken: String,
    onTextTokenChange: (String) -> Unit,
    textHexLight: String,
    textHexDark: String,
    textWeight: String,
    onTextWeightChange: (String) -> Unit,
    textShadow: Boolean,
    onTextShadowChange: (Boolean) -> Unit,
    onEditHexClick: () -> Unit
) {
    val tokens = listOf("Primary", "OnPrimary", "Secondary", "OnSecondary", "Surface", "OnSurface", "Error")
    var expandedToken by remember { mutableStateOf(false) }

    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessLow)), 
        shape = RoundedCornerShape(24.dp), 
        colors = CardDefaults.cardColors(containerColor = cardColor), 
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) 
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Text Style",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
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
                targetState = colorType,
                label = "TextColorTypeAnimation"
            ) { type ->
                if (type == "TOKEN") {
                    TokenDropdown(
                        label = "Text Color",
                        selected = textToken,
                        expanded = expandedToken,
                        onExpandedChange = { expandedToken = it },
                        tokens = tokens,
                        onSelect = onTextTokenChange
                    )
                } else {
                    ColorPreviewButton(
                        label = "Text Hex Colors",
                        lightHex = textHexLight,
                        darkHex = textHexDark
                    ) { onEditHexClick() }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Font Weight", style = MaterialTheme.typography.bodySmall)
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                SegmentedButton(
                    selected = textWeight == "NORMAL",
                    onClick = { onTextWeightChange("NORMAL") },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
                ) { Text("Normal") }
                SegmentedButton(
                    selected = textWeight == "MEDIUM",
                    onClick = { onTextWeightChange("MEDIUM") },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                ) { Text("Medium") }
                SegmentedButton(
                    selected = textWeight == "BOLD",
                    onClick = { onTextWeightChange("BOLD") },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                ) { Text("Bold") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    Text("Drop Shadow", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Improves readability on bright backgrounds",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = textShadow, onCheckedChange = onTextShadowChange)
            }
        }
    }
}
