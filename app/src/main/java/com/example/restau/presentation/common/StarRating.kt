package com.example.restau.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restau.R
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.RestaUTheme
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun StarRating(
    value: Double,
    size: Dp,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    showValue: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier
    ) {
        val integerPart = floor(value).roundToInt()
        val missing = if ((5 - integerPart - 1) >= 0) 5 - integerPart - 1 else 0
        val remainder = value - integerPart
        repeat(integerPart) {
            Star("full", Modifier.size(size), tint)
        }
        if (integerPart != 5) {
            if (remainder < 0.25) {
                Star("empty", Modifier.size(size), tint)
            } else {
                Star("half", Modifier.size(size), tint)
            }
        }
        repeat(missing) {
            Star("empty", Modifier.size(size), tint)
        }
        Spacer(modifier = Modifier.width(3.dp))
        if (showValue) {
            Text(
                text = "($value)",
                fontFamily = Poppins,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun Star(
    type: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    when (type) {
        "full" -> {
            Icon(
                painter = painterResource(id = R.drawable.filledstar),
                contentDescription = "star",
                tint = tint,
                modifier = modifier.clickable(interactionSource = interactionSource, indication = null) {
                    onClick()
                }
            )
        }

        "empty" -> {
            Icon(
                painter = painterResource(id = R.drawable.outlinedstar),
                contentDescription = "empty star",
                tint = tint,
                modifier = modifier.clickable(interactionSource = interactionSource, indication = null) {
                    onClick()
                }
            )
        }
        else -> {
            Icon(
                painter = painterResource(id = R.drawable.halffilledstar),
                contentDescription = "half star",
                tint = tint,
                modifier = modifier.clickable(interactionSource = interactionSource, indication = null) {
                    onClick()
                }
            )
        }

    }
    Spacer(modifier = Modifier.width(5.dp))
}

@Preview(showBackground = true)
@Composable
fun StarsReviewPreview() {
    RestaUTheme {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column {
                StarRating(value = 5.0, 21.dp)
                StarRating(value = 4.7, 21.dp)
                StarRating(value = 4.5, 21.dp)
                StarRating(value = 4.25, 21.dp)
                StarRating(value = 4.1, 21.dp)
                StarRating(value = 4.0, 21.dp)
                StarRating(value = 3.7, 21.dp)
                StarRating(value = 3.5, 21.dp)
                StarRating(value = 3.25, 21.dp)
                StarRating(value = 3.0, 21.dp)
                StarRating(value = 2.75, 21.dp)
                StarRating(value = 2.5, 21.dp)
                StarRating(value = 2.25, 21.dp)
                StarRating(value = 2.0, 21.dp)
                StarRating(value = 1.75, 21.dp)
                StarRating(value = 1.5, 21.dp)
                StarRating(value = 1.25, 21.dp)
                StarRating(value = 1.0, 21.dp)
                StarRating(value = 0.75, 21.dp)
                StarRating(value = 0.5, 21.dp)
                StarRating(value = 0.25, 21.dp)
                StarRating(value = 0.0, 21.dp)

            }
        }
    }
}