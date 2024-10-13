package com.example.restau.domain.usecases.restaurantUseCases

class RestaurantUseCases(
    val getRestaurants: GetRestaurants,
    val getOpenRestaurants: GetOpenRestaurants,
    val getIsNewRestaurantArray: GetIsNewRestaurantArray,
    val getFilterRestaurantsByNameAndCategories: GetFilterRestaurantsByNameAndCategories,
    val getRestaurantsInRadius: GetRestaurantsInRadius,
    val getRestaurantsLiked: GetRestaurantsLiked,
    val hasLikedCategoriesArray: HasLikedCategoriesArray
)