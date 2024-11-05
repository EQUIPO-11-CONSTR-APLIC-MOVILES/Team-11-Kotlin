package com.example.restau.domain.usecases.reviewsUseCases

import com.example.restau.domain.model.Review
import com.example.restau.domain.repository.ReviewsRepository

class GetRestaurantsReviews(
    private val reviewsRepository: ReviewsRepository
) {

    suspend operator fun invoke(restaurantId: String): List<Review> {
        return reviewsRepository.getRestaurantsReviews(restaurantId)
    }

}