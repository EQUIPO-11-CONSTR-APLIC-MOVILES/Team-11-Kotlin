package com.example.restau.presentation.restaurant

sealed class RestaurantEvent {
    data class ScreenLaunched(val restaurantID: String): RestaurantEvent()
    data class SendLike(val restaurantID: String): RestaurantEvent()
    data object ShowSchedule: RestaurantEvent()

}