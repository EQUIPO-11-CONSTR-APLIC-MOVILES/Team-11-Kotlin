package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.User
import com.example.restau.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            suspendCancellableCoroutine<Boolean> { continuation ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(true)
                            Log.d("AuthRepository", "signInWithEmail:success")
                        } else {
                            val exception = task.exception
                            Log.d("AuthRepository", "signInWithEmail:failure", exception)
                            continuation.resume(false)
                        }
                    }
                }
        } catch (e: Exception) {
            Log.w("AuthRepository", "signInWithEmail:failure32", e)
            false
        }
    }

    override suspend fun register(email: String, password: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUserTokenID(): String? {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                suspendCancellableCoroutine<String?> { continuation ->
                    currentUser.getIdToken(true)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(task.result?.token)
                            } else {
                                continuation.resume(null)
                            }
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "getCurrentUserTokenID: failure", e)
            null
        }
    }
}
