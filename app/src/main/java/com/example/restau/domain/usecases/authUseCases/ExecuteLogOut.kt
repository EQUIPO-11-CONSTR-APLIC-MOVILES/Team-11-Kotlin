package com.example.restau.domain.usecases.authUseCases

import com.example.restau.domain.repository.AuthRepository

class ExecuteLogOut(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Boolean {
        return authRepository.logOut()
    }
}