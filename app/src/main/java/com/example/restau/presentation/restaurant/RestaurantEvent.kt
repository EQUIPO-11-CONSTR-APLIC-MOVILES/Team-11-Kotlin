package com.example.restau.presentation.restaurant

import android.content.Context

sealed class RestaurantEvent {
    data class ScreenLaunched(val restaurantID: String): RestaurantEvent()
    data class SendLike(val restaurantID: String): RestaurantEvent()
    data object ShowSchedule: RestaurantEvent()
    data object ScreenOpened: RestaurantEvent()
    data object ScreenClosed: RestaurantEvent()
    data class LaunchMaps(val lat: Double, val lon: Double, val name: String, val place: String, val context: Context): RestaurantEvent()
}