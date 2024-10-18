package com.example.restau.domain.usecases.analyticsUseCases

import com.example.restau.domain.repository.LikeDateRestaurantRepository

class SendLikeDateRestaurantEvent(
    private val likeDateRestaurantRepository: LikeDateRestaurantRepository
) {

    suspend operator fun invoke(restaurantId: String) {
        likeDateRestaurantRepository.sendLikeDateRestaurant(restaurantId)
    }

}