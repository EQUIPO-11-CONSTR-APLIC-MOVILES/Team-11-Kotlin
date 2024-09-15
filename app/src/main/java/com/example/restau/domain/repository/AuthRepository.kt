package com.example.restau.domain.repository

import com.example.restau.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User?
    suspend fun register(email: String, password: String): User?
}
