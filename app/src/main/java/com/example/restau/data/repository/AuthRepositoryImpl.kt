package com.example.restau.data.repository

import com.example.restau.domain.model.User
import com.example.restau.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl (private val firebaseAuth: FirebaseAuth) : AuthRepository {

    override suspend fun login(email: String, password: String): User? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                User(it.uid, it.email ?: "")
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun register(email: String, password: String): User? {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let {
                User(it.uid, it.email ?: "")
            }
        } catch (e: Exception) {
            null
        }
    }
}