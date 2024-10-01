package com.example.restau.presentation.liked

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect

@Composable
fun LikedScreen(
    likedViewModel: LikedViewModel = hiltViewModel()
) {
    LifecycleResumeEffect(Unit) {
        likedViewModel.onEvent(LikedEvent.ScreenOpened)

        onPauseOrDispose {
            likedViewModel.onEvent(LikedEvent.ScreenClosed)
        }
    }
}