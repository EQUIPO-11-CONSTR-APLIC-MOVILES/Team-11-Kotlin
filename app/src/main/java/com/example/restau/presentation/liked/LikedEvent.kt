package com.example.restau.presentation.liked

sealed class LikedEvent {

    data object ScreenOpened: LikedEvent()
    data object ScreenLaunched: LikedEvent()
    data object ScreenClosed: LikedEvent()
    data class SendLike(val documentId: String, val delete: Boolean): LikedEvent()
    data class ChangeIsNavigatingUserDetail(val isNavigating: Boolean): LikedEvent()

}
