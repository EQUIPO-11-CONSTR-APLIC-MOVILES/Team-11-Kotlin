package com.example.restau.domain.usecases

import com.example.restau.domain.model.User
import com.example.restau.domain.repository.AuthRepository

class LoginUseCases (private val authRepository: AuthRepository) {
    suspend fun execute(email: String, password: String): User? {
        return authRepository.login(email, password)
    }
}