package com.example.restau.presentation.signin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.authUseCases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val analyticsUseCases: AnalyticsUseCases
): ViewModel() {

    var state by mutableStateOf(SignInState())

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.SignIn -> {
                state = state.copy(isLoading = true)
                signIn(event)
            }
            is SignInEvent.EmailChange -> {
                state = state.copy(email = event.email)
            }
            is SignInEvent.PasswordChange -> {
                state = state.copy(password = event.password)
            }
            is SignInEvent.ShowPasswordChange -> {
                state = state.copy(showPassword = event.showPassword)
            }

            is SignInEvent.FeatureInteraction -> sendFeatureInteractionEvent(event.featureName)
        }
    }

    private fun signIn(event: SignInEvent.SignIn){
        viewModelScope.launch {
            try {
                val isAuthenticated = executeSignIn(event.email, event.password, event.authCheck)

                if (isAuthenticated) {
                    event.onSuccess()
                }
                state = state.copy(isLoading = false)
            } catch (e: Exception) {
                Log.e("SignInViewModel", "Error during sign-in", e)
            }
        }
    }

    private suspend fun executeSignIn(email: String, password: String, authCheck: suspend () -> Unit): Boolean {
        return withContext(Dispatchers.IO) {
            var authenticated = false
            val result = authUseCases.executeSignIn(email, password)
            result.onSuccess {
                authenticated = true
            }.onFailure {
                signInFailure(it.message ?: "An error occurred")
            }
            Log.d("SignInViewModel", "executeSignIn: $authenticated")
            authCheck()
            authenticated
        }
    }

    private fun signInFailure(message: String) {
        state = state.copy(errSignIn = message)
    }

    private fun sendFeatureInteractionEvent(featureName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsUseCases.sendFeatureInteraction(featureName, "login_user")
        }
    }
}