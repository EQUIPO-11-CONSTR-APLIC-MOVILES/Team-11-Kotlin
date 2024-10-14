package com.example.restau.presentation.reviewlist

sealed class ReviewListEvent() {

    data class ScreenLaunched(val restaurantId: String): ReviewListEvent()
    data class StarPressed(val value: Double): ReviewListEvent()

}