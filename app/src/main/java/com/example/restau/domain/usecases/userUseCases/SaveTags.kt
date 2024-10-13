package com.example.restau.domain.usecases.userUseCases

import com.example.restau.domain.model.User
import com.example.restau.domain.repository.UsersRepository

class SaveTags(private val usersRepository: UsersRepository) {
    suspend operator fun invoke(tags: List<String>, user: User) {
        usersRepository.saveTags(tags, user)
    }
}