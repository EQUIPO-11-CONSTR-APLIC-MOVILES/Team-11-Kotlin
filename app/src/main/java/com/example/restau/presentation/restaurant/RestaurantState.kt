package com.example.restau.presentation.restaurant

import com.example.restau.domain.model.Restaurant

data class RestaurantState (
    val restaurant: Restaurant = Restaurant(),
    val showSchedule: Boolean = false,
    val isOpen: Boolean = true,
    val match: Float? = null,
)