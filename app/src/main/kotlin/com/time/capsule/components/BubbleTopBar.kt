package com.kshitiz.capsule.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BubbleTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    version: String = "v2.0.2",
    onChipClick: () -> Unit
) {
    val topBarColor = if (isSystemInDarkTheme()) 
        MaterialTheme.colorScheme.surfaceContainer 
    else MaterialTheme.colorScheme.surfaceContainerLow

    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = topBarColor,
            scrolledContainerColor = topBarColor
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Capsule", fontWeight = FontWeight.Bold)
                VersionChip(label = "$version by Kshitiz", onClick = onChipClick)
            }
        }
    )
}

@Composable
private fun VersionChip(label: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        label = "scale"
    )

    Surface(
        modifier = Modifier.padding(top = 2.dp).scale(scale).clickable(
            interactionSource = interactionSource, 
            indication = null, 
            onClick = onClick
        ),
        color = MaterialTheme.colorScheme.tertiary,
        shape = CircleShape
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiary,
            fontWeight = FontWeight.Medium
        )
    }
}
