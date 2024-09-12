package com.example.restau.domain.usecases

import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.repository.RestaurantsRepository

class GetOpenRestaurants(
    val restaurantsRepository: RestaurantsRepository
) {


    suspend operator fun invoke(day: String, time: Int): List<Restaurant> {
        return restaurantsRepository.getOpenRestaurants(day, time)
    }


}