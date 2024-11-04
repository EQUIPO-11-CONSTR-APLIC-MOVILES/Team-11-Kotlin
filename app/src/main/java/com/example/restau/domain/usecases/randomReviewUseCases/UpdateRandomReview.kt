package com.example.restau.domain.usecases.randomReviewUseCases

import com.example.restau.domain.repository.RandomReviewRepository

class UpdateRandomReview(private val randomReviewRepository: RandomReviewRepository) {
    suspend operator fun invoke(randomID: String): Boolean{
        return randomReviewRepository.updateRandomReview(randomID)
    }
}