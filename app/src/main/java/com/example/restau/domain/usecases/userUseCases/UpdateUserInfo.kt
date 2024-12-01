package com.example.restau.domain.usecases.userUseCases

import com.example.restau.domain.model.User
import com.example.restau.domain.repository.UsersRepository

class UpdateUserInfo(private val userRepository: UsersRepository) {
    suspend operator fun invoke(user: User): Boolean {
        return userRepository.updateUserInfo(user)
    }

}