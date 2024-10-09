package com.example.restau.domain.usecases.authUseCases

import com.example.restau.domain.repository.AuthRepository

class ExecuteSignIn (private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        return authRepository.signIn(email, password)
    }
}