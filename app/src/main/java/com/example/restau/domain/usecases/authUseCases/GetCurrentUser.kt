package com.example.restau.domain.usecases.authUseCases

import com.example.restau.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class GetCurrentUser (private val authRepository: AuthRepository) {
    suspend operator fun invoke(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }
}