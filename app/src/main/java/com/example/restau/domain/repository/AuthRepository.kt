package com.example.restau.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Result<String>
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun setUserInfo(name: String, email: String, picLink: String): Boolean
}
