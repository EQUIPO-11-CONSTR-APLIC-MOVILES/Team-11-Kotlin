package com.example.restau.domain.usecases

import com.example.restau.domain.repository.AuthRepository

class SetUserInfo(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, picLink: String): Boolean {
        return authRepository.setUserInfo(name, email, picLink)
    }

}