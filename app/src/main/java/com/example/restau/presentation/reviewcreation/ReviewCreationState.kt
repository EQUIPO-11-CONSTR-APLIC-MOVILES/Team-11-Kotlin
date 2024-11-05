package com.example.restau.presentation.reviewcreation

import com.example.restau.domain.model.Review

data class ReviewCreationState(
    val review: Review = Review(),
    val isLoading: Boolean = true,
    val isReviewAddedSuccessfully: Boolean = false
)