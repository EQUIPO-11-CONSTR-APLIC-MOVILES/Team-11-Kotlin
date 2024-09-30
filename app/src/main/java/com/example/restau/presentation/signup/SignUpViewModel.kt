package com.example.restau.presentation.signup

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
class SignUpViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {

    var state by mutableStateOf(SignUpState())

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.SignUp -> {
                signUp(event)
            }
        }
    }

    private fun signUp(event: SignUpEvent.SignUp){
        viewModelScope.launch {
            try {
                val isAuthenticated = executeSignUp(event.email, event.password, event.authCheck)

                if (isAuthenticated) {
                    event.onSuccess()
                    authUseCases.setUserInfo(event.name, event.email, getUserRandPic())
                }
            } catch (e: Exception) {
                Log.e("SignInViewModel", "Error during sign-up", e)
            }
        }
    }

    private suspend fun executeSignUp(email: String, password: String, authCheck: suspend () -> Unit): Boolean {
        return withContext(Dispatchers.IO) {
            var authenticated = false
            val result = authUseCases.executeSignUp(email, password)
            result.onSuccess {
                authenticated = true
            }.onFailure {
                signUpFailure(it.message ?: "An error occurred")
                authenticated = false
            }
            Log.d("SignUpViewModel", "executeSignUp: $authenticated")
            authCheck()
            authenticated
        }
    }

    private fun signUpFailure(message: String) {
        state = state.copy(errSignUp = message)
    }

    private fun getUserRandPic(): String {
        val pictureLinks = arrayOf(
            "gs://restau-5dba7.appspot.com/profilePics/alien.svg",
            "gs://restau-5dba7.appspot.com/profilePics/ant.svg",
            "gs://restau-5dba7.appspot.com/profilePics/astronaut.svg",
            "gs://restau-5dba7.appspot.com/profilePics/bee.svg",
            "gs://restau-5dba7.appspot.com/profilePics/cat.svg"
        )
        val rnds = (0..4).random()
        return pictureLinks[rnds]
    }
}