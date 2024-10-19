package com.example.restau.domain.usecases.userUseCases

import com.example.restau.domain.model.User
import com.example.restau.domain.repository.UsersRepository

class SendLike(
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(user: User) {
        usersRepository.submitLike(user)
    }

}