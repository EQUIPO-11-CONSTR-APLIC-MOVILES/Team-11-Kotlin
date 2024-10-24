package com.example.restau.presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun StarPicker(
    onStarSelect: (Int) -> Unit,
    value: Int,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        for (i in 1..5) {
            Star(
                pickType(value, i.toDouble()),
                Modifier.size(size),
                pickTint(value, i.toDouble()),
                onClick = {
                    onStarSelect(i)
                }
            )
        }
    }
}

private fun pickType(value: Int, target: Double): String {
    return if (value >= target) "full" else "empty"
}

@Composable
private fun pickTint(value: Int, target: Double): Color {
    return if (value >= target)  MaterialTheme.colorScheme.primary else Color(0xFFB9B9B9)
}

