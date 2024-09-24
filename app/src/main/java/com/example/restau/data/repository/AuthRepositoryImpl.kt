package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.User
import com.example.restau.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            authResult.user != null
        } catch (e: Exception) {
            Log.e("AuthRepository", "signInWithEmail:failure", e)
            false
        }
    }


    override suspend fun register(email: String, password: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return try {
            firebaseAuth.currentUser
        } catch (e: Exception) {
            Log.e("AuthRepository", "getCurrentUserTokenID: failure", e)
            null
        }
    }
}
