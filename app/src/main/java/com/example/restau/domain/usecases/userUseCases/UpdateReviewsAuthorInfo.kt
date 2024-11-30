package com.example.restau.domain.usecases.userUseCases

import com.example.restau.domain.repository.UsersRepository

class UpdateReviewsAuthorInfo(private val userRepository: UsersRepository) {
    suspend operator fun invoke(documentId: String, authorName: String, authorPFP: String): Boolean {
        return userRepository.updateReviewsAuthorInfo(documentId, authorName, authorPFP)
    }

}