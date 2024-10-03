package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
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


    override suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                Result.success("Sign-up successful")
            } else {
                Result.failure(Exception("Unknown error"))
            }
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(Exception("Weak password"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Invalid credentials"))
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("User already exists"))
        } catch (e: Exception) {
            Result.failure(Exception("Sign-up failed: ${e.message}"))
        }
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return try {
            firebaseAuth.currentUser
        } catch (e: Exception) {
            Log.e("AuthRepository", "getCurrentUserToken: failure", e)
            null
        }
    }
}
