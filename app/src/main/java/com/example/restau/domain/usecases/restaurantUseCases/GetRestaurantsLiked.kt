package com.example.restau.domain.usecases.restaurantUseCases

import com.example.restau.domain.model.Restaurant

class GetRestaurantsLiked {

    operator fun invoke(restaurants: List<Restaurant>, liked: List<String>): List<Boolean> {
        return restaurants.map { liked.contains(it.documentId) }
    }

}