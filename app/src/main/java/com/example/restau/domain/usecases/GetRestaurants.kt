package com.example.restau.domain.usecases

import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.repository.RestaurantsRepository

class GetRestaurants(
    private val restaurantsRepository: RestaurantsRepository
) {

    suspend operator fun invoke(): List<Restaurant> {
        return restaurantsRepository.getAllRestaurants()
    }

}