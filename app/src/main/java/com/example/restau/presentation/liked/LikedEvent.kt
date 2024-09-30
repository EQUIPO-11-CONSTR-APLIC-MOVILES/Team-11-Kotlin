package com.example.restau.presentation.liked

sealed class LikedEvent {

    data object ScreenOpened: LikedEvent()
    data object ScreenClosed: LikedEvent()

}