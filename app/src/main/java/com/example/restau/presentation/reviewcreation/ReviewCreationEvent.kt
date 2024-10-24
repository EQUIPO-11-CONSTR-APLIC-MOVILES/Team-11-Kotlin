package com.example.restau.presentation.reviewcreation

import com.example.restau.domain.model.Review

sealed class ReviewCreationEvent() {
    data object ScreenLaunched : ReviewCreationEvent()
    data class AddReviewEvent(val review: Review) : ReviewCreationEvent()
    data class ReviewTextChange(val reviewText: String) : ReviewCreationEvent()
    data class ReviewRatingChange(val reviewRating: Int) : ReviewCreationEvent()
    data class ShowErrorDialog(val show: Boolean) : ReviewCreationEvent()
}