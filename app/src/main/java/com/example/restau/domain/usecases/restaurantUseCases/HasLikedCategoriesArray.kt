package com.example.restau.domain.usecases.restaurantUseCases

import com.example.restau.domain.model.Restaurant

class HasLikedCategoriesArray {

    operator fun invoke(restaurants: List<Restaurant>, categories: List<String>): List<Boolean> {
        return restaurants.map {
            it.categories.any {category ->
                categories.contains(category)
            }
        }
    }

}