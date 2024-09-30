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
                    authUseCases.setUserInfo(event.name, event.email)
                } else {
                    signUpFailure()
                }
            } catch (e: Exception) {
                Log.e("SignInViewModel", "Error during sign-up", e)
            }
        }
    }

    private suspend fun executeSignUp(email: String, password: String, authCheck: suspend () -> Unit): Boolean {
        return withContext(Dispatchers.IO) {
            val authenticated = authUseCases.executeSignUp(email, password)
            Log.d("SignUpViewModel", "executeSignUp: $authenticated")
            authCheck()
            authenticated
        }
    }

    private fun signUpFailure() {
        state = state.copy(errSignUp = true)
    }

    private fun getUserRandPic() {
        val pictureLinks = arrayOf(
            "link1",
            "link2",
            "link3"
        )
    }
}