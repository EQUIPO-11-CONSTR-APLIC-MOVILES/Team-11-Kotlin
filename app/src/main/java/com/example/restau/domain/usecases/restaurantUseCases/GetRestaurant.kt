package com.example.restau.domain.usecases.restaurantUseCases

import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.repository.RestaurantsRepository

class GetRestaurant (
    private val restaurantsRepository: RestaurantsRepository
) {
    suspend operator fun invoke(id: String): Restaurant {
        return restaurantsRepository.getRestaurant(id)
    }
}