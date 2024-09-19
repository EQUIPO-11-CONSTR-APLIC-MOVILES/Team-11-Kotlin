package com.example.restau.domain.usecases

import com.example.restau.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

class GetCurrentUser (private val authRepository: AuthRepository) {
    suspend operator fun invoke(): StateFlow<FirebaseUser?> {
        return authRepository.getCurrentUser()
    }
}