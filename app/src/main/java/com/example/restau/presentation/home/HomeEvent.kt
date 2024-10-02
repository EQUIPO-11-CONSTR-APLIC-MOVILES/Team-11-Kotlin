package com.example.restau.presentation.home

sealed class HomeEvent {

    data class FilterEvent(val selectedFilter: Int): HomeEvent()
    data class SendLike(val documentId: String, val delete: Boolean): HomeEvent()
    data object ScreenOpened: HomeEvent()
    data object ScreenLaunched: HomeEvent()
    data object ScreenClosed: HomeEvent()

}