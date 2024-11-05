package com.example.restau.domain.usecases.reviewsUseCases

import com.example.restau.domain.model.Review
import com.example.restau.domain.repository.ReviewsRepository

class AddReview(
    private val reviewsRepository: ReviewsRepository
) {

    suspend operator fun invoke(review: Review): Boolean {
        return reviewsRepository.addReview(review)
    }

}