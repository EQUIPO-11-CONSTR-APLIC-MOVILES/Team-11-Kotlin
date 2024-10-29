package com.example.restau.presentation.home

import com.example.restau.domain.model.Restaurant

data class HomeState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLiked: List<Boolean> = emptyList(),
    val isNew: List<Boolean> = emptyList(),
    val nothingOpen: Boolean = false,
    val nothingforyou: Boolean = false,
    val selectedFilter: Int = 0,
    val isFeatured: List<Boolean> = emptyList()
)