package com.example.restau.presentation.reviewlist

import com.example.restau.domain.model.Review

data class ReviewListState(
    val reviews: List<Review> = emptyList(),
    val reviewValue: Double = 0.0,
    val isLoading: Boolean = true
)