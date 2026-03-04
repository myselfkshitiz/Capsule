package com.kshitiz.capsule.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CornerRadiusControlCard(
    cornerTL: Int,
    onCornerTLChange: (Int) -> Unit,
    cornerTR: Int,
    onCornerTRChange: (Int) -> Unit,
    cornerBL: Int,
    onCornerBLChange: (Int) -> Unit,
    cornerBR: Int,
    onCornerBRChange: (Int) -> Unit
) {
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
                text = "Corner Radius",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            RadiusSliderRow("Top Left", "Curve for the upper left corner", cornerTL) { onCornerTLChange(it.toInt()) }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            
            RadiusSliderRow("Top Right", "Curve for the upper right corner", cornerTR) { onCornerTRChange(it.toInt()) }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            
            RadiusSliderRow("Bottom Left", "Curve for the lower left corner", cornerBL) { onCornerBLChange(it.toInt()) }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            
            RadiusSliderRow("Bottom Right", "Curve for the lower right corner", cornerBR) { onCornerBRChange(it.toInt()) }
        }
    }
}

@Composable
private fun RadiusSliderRow(title: String, description: String, value: Int, onValueChange: (Float) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
                Text(text = description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(text = "$value px", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = onValueChange,
            valueRange = 0f..100f
        )
    }
}
