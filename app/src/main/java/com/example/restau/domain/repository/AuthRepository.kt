package com.example.restau.domain.repository

import com.example.restau.domain.model.User

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun register(email: String, password: String): User?
    suspend fun getCurrentUserTokenID(): String?
}
