package com.example.restau.presentation.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restau.R
import com.example.restau.ui.theme.RestaUTheme

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    0f to MaterialTheme.colorScheme.tertiary,
                    1f to MaterialTheme.colorScheme.secondary,
                    radius = 850f
                ),
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.restau),
            contentDescription = "RestaU logo",
            modifier = Modifier
                .align(
                    Alignment.Center
                ).size(300.dp)
        )
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    RestaUTheme {
        SplashScreen()
    }
}