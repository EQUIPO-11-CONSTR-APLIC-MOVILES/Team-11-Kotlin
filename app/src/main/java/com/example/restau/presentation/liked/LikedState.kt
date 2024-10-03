package com.example.restau.presentation.liked

import com.example.restau.domain.model.Restaurant

data class LikedState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLiked: List<Boolean> = emptyList(),
    val isNew: List<Boolean> = emptyList(),
)
