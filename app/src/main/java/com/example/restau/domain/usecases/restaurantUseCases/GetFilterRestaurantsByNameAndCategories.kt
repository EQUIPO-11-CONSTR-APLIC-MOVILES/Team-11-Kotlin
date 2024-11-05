package com.example.restau.domain.usecases.restaurantUseCases

import com.example.restau.domain.model.Restaurant

class GetFilterRestaurantsByNameAndCategories {

    operator fun invoke(restaurants: List<Restaurant>, restaurantName: String): List<Restaurant> {
        return restaurants.filter {
            it.name.contains(restaurantName, ignoreCase = true) ||
                    it.categories.any { category ->
                        category.contains(restaurantName, ignoreCase = true)
                    }
        }
    }

}