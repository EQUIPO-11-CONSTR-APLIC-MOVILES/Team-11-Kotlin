package com.example.restau.domain.repository

import com.example.restau.domain.model.User

interface UsersRepository {

    suspend fun getUser(email: String): User

    suspend fun submitLike(restaurant: String, user: User)

    suspend fun saveTags(tags: List<String>, user: User)
}