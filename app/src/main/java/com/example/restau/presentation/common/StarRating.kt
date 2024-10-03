package com.example.restau.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        val integerPart = floor(value).roundToInt()
        val missing = if ((5 - integerPart - 1) >= 0) 5 - integerPart - 1 else 0
        val remainder = value - integerPart
        repeat(integerPart) {
            Star("full", Modifier.size(size))
        }
        if (integerPart != 5) {
            if (remainder < 0.25) {
                Star("empty", Modifier.size(size))
            } else {
                Star("half", Modifier.size(size))
            }
        }
        repeat(missing) {
            Star("empty", Modifier.size(size))
        }
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "($value)",
            fontFamily = Poppins,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun Star(
    type: String,
    modifier: Modifier = Modifier
) {
    when (type) {
        "full" -> {
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "star",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier
            )
        }

        "empty" -> {
            Icon(
                painter = painterResource(id = R.drawable.empty_star),
                contentDescription = "empty star",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier
            )
        }
        else -> {
            Icon(
                painter = painterResource(id = R.drawable.half_star),
                contentDescription = "half star",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier
            )
        }

    }
    Spacer(modifier = Modifier.width(1.dp))
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