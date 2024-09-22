package com.example.restau.presentation.signin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var state by mutableStateOf(SignInState())

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.SignIn -> {
                signIn(event)
            }
        }
    }

    private fun signIn(event: SignInEvent.SignIn){
        viewModelScope.launch {
            try {
                val isAuthenticated = executeSignIn(event.email, event.password)

                if (isAuthenticated) {
                    event.onSuccess()
                } else {
                    signInFailure()
                }
            } catch (e: Exception) {
                Log.e("SignInViewModel", "Error during sign-in", e)
            }
        }
    }

    private suspend fun executeSignIn(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val authenticated = authUseCases.executeSignIn(email, password)
            authenticated
        }
    }

    private fun signInFailure() {
        state = state.copy(errSignIn = true)
    }
}