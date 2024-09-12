package com.example.restau.presentation.home

import com.example.restau.domain.model.Restaurant

data class HomeState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLiked: List<Boolean> = emptyList(),
    val selectedFilter: Int = 0,
)