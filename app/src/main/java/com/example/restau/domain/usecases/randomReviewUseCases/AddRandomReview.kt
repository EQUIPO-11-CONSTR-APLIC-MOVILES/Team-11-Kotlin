package com.example.restau.domain.usecases.randomReviewUseCases

import com.example.restau.domain.repository.RandomReviewRepository

class AddRandomReview(private val randomReviewRepository: RandomReviewRepository) {
    suspend operator fun invoke(): String? {
        return randomReviewRepository.addRandomReview()
    }
}