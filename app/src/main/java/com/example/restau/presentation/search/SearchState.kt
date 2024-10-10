package com.example.restau.presentation.search

import com.example.restau.domain.model.Restaurant

data class SearchState(
    val restaurants: List<Restaurant> = emptyList(),
    val filteredRestaurantsByNameAndCategories: List<Restaurant> = emptyList(),
    val recentRestaurants: List<Restaurant> = emptyList(),
    val isLiked: List<Boolean> = emptyList(),
    val isNew: List<Boolean> = emptyList(),
    val nothingOpen: Boolean = false,
    val selectedFilter: Int = 0,
)