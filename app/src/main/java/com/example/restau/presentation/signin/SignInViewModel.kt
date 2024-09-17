package com.example.restau.presentation.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.SignIn -> {
                viewModelScope.launch {
                    try {
                        val isAuthenticated = signIn(event.email, event.password)

                        if (isAuthenticated) {
                            signInSuccess()
                            Log.d("SignInViewModel", "User Authenticated $event.email")
                        } else {
                            signInFailure()
                            Log.d("SignInViewModel", "User not Authenticated $event.email")
                        }
                    } catch (e: Exception) {
                        Log.e("SignInViewModel", "Error during sign-in", e)
                    }
                }
            }
        }
    }

    private suspend fun signIn(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val authenticated = authUseCases.executeSignIn(email, password)
            authenticated
        }
    }

    private suspend fun getCurrentUserTokenID(): String? {
        return withContext(Dispatchers.IO) {
            authUseCases.getCurrentUser()
        }
    }

    private suspend fun signInSuccess() {
        AuthUseCases.isSignedIn = (getCurrentUserTokenID() != null)
    }

    private fun signInFailure() {
        AuthUseCases.isSignedIn = false
        AuthUseCases.errSignIn = true
    }
}