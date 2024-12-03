package com.example.restau.domain.usecases.userUseCases

import android.net.Uri
import com.example.restau.domain.model.User
import com.example.restau.domain.repository.UsersRepository

class UploadUserImage(private val userRepository: UsersRepository) {
    suspend operator fun invoke(user: User, uri: Uri): String? {
        return userRepository.uploadProfileImage(user, uri)
    }
}