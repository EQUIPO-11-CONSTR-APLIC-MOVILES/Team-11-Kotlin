package com.example.restau.domain.usecases.restaurantUseCases

import com.example.restau.domain.model.Restaurant

class GetFeaturedArray {

    operator fun invoke(restaurants: List<Restaurant>, featuredNames: List<String>): List<Boolean> {
        return restaurants.map { featuredNames.contains(it.name) }
    }

}