package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.User
import com.example.restau.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume


class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            suspendCancellableCoroutine { continuation ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            val exception = task.exception
                            continuation.resume(false)
                            Log.e("AuthRepository", "signInWithEmail:failure", exception)
                        }
                    }
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "signInWithEmail:failure", e)
            false
        }
    }

    override suspend fun register(email: String, password: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUserTokenID(): String? {
        return try {
            val currentUser = firebaseAuth.currentUser
            currentUser?.getIdToken(true)?.await()?.token
        } catch (e: Exception) {
            Log.e("AuthRepository", "getCurrentUserTokenID: failure", e)
            null
        }
    }
}
