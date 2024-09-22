package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.User
import com.example.restau.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    override suspend fun getCurrentUser(): Flow<FirebaseUser?> {
        return try {
            val currentUserMSF = MutableStateFlow(firebaseAuth.currentUser)
            val currentUser: Flow<FirebaseUser?> = currentUserMSF

            firebaseAuth.addAuthStateListener { auth ->
                currentUserMSF.value = auth.currentUser
            }

            currentUser
        } catch (e: Exception) {
            Log.e("AuthRepository", "getCurrentUserTokenID: failure", e)
            MutableStateFlow<FirebaseUser?>(null).asStateFlow()
        }
    }
}
