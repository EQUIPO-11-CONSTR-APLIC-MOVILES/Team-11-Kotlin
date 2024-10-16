package com.example.restau.presentation.reviewlist

import com.example.restau.domain.model.Review

data class ReviewListState(
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = true
)