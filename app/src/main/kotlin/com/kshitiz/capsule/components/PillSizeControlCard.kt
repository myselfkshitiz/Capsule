package com.kshitiz.capsule.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PillSizeControlCard(
    pillWidth: Int,
    onWidthChange: (Int) -> Unit,
    pillHeight: Int,
    onHeightChange: (Int) -> Unit,
    textSize: Int,
    onTextSizeChange: (Int) -> Unit,
    keepRatio: Boolean,
    onKeepRatioChange: (Boolean) -> Unit
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
        Column(modifier = Modifier.padding(vertical = 20.dp)) {
            Text(
                text = "Dimensions & Text",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .toggleable(
                        value = keepRatio,
                        onValueChange = onKeepRatioChange,
                        role = Role.Switch
                    )
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                    Text("Keep Ratio", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Scale pill width and height together",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = keepRatio, onCheckedChange = null)
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp), color = MaterialTheme.colorScheme.surfaceVariant)

            AnimatedContent(
                targetState = keepRatio,
                label = "RatioSliderAnimation",
                modifier = Modifier.padding(horizontal = 20.dp)
            ) { isKeepRatio ->
                Column {
                    if (isKeepRatio) {
                        SliderRow(
                            title = "Overall Size",
                            description = "Adjust the primary scale of the pill",
                            value = pillWidth,
                            onValueChange = {
                                val w = it.toInt()
                                onWidthChange(w)
                                onHeightChange((w * 0.4f).toInt())
                            },
                            range = 50f..300f
                        )
                    } else {
                        SliderRow(
                            title = "Pill Width",
                            description = "Set the horizontal length",
                            value = pillWidth,
                            onValueChange = { onWidthChange(it.toInt()) },
                            range = 50f..300f
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SliderRow(
                            title = "Pill Height",
                            description = "Set the vertical thickness",
                            value = pillHeight,
                            onValueChange = { onHeightChange(it.toInt()) },
                            range = 20f..150f
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)

            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                SliderRow(
                    title = "Text Size",
                    description = "Adjust the inner typography scale",
                    value = textSize,
                    onValueChange = { onTextSizeChange(it.toInt()) },
                    range = 10f..40f
                )
            }
        }
    }
}

@Composable
private fun SliderRow(
    title: String,
    description: String,
    value: Int,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value.toFloat(),
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
