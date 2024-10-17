package com.example.restau.presentation.restaurant

sealed class RestaurantEvent {
    data class ScreenLaunched(val restaurantId: String): RestaurantEvent()
}