package com.example.restau.domain.usecases

class RestaurantUseCases(
    val getRestaurants: GetRestaurants,
    val getOpenRestaurants: GetOpenRestaurants,
    val getIsNewRestaurantArray: GetIsNewRestaurantArray,
    val getRestaurantsInRadius: GetRestaurantsInRadius
)