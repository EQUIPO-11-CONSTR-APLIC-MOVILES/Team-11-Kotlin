package com.example.restau.domain.usecases

import com.example.restau.domain.repository.AuthRepository

class GetCurrentUser (private val authRepository: AuthRepository) {
    suspend operator fun invoke(): String? {
        return authRepository.getCurrentUserTokenID()
    }
}