package com.example.restau.domain.usecases.userUseCases

import android.util.Log
import com.example.restau.domain.model.User
import com.example.restau.domain.repository.AuthRepository
import com.example.restau.domain.repository.UsersRepository


class GetUserObject(
    private val usersRepository: UsersRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): User {
        Log.d("DONITEST", "INVOKE GET USER OBJECT")
        val user = authRepository.getCurrentUser()
        Log.d("DONITEST", user?.email ?: "")
        val userObject =  usersRepository.getUser(user?.email ?: "")
        return userObject
    }


}