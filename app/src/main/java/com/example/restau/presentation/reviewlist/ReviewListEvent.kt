package com.example.restau.presentation.reviewlist

sealed class ReviewListEvent() {

    data class ScreenLaunched(val restaurantId: String): ReviewListEvent()
    data object ScreenOpened: ReviewListEvent()
    data object ScreenClosed: ReviewListEvent()
    data class TempReviewRatingChange(val tempRating: Int): ReviewListEvent()
}