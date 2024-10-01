package com.example.restau.domain.usecases

import com.example.restau.domain.repository.AuthRepository

class ExecuteSignUp(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<String> {
        return authRepository.signUp(email, password)
    }
}