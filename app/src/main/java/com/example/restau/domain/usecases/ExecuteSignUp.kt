package com.example.restau.domain.usecases

import com.example.restau.domain.repository.AuthRepository

class ExecuteSignUp(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        return authRepository.signUp(email, password)
    }
}