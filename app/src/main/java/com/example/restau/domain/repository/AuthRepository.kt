package com.example.restau.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<String>
    suspend fun signUp(email: String, password: String): Result<String>
    suspend fun logOut(): Boolean
    suspend fun getCurrentUser(): FirebaseUser?
}
