package com.example.restau.domain.usecases.userUseCases

import com.example.restau.domain.repository.UsersRepository

class SetUserInfo(private val userRepository: UsersRepository) {
    suspend operator fun invoke(name: String, email: String, picLink: String, userID: String): Boolean {
        return userRepository.setUserInfo(name, email, picLink, userID)
    }

}