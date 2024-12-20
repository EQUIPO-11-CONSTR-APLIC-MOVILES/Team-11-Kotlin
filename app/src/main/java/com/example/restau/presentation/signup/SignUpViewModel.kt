package com.example.restau.presentation.signup

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.authUseCases.AuthUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import com.example.restau.utils.getConnectivityAsStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val analyticsUseCases: AnalyticsUseCases,
    private val application: Application
): ViewModel() {

    var state by mutableStateOf(SignUpState())

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.SignUp -> {
                state = state.copy(isLoading = true)
                signUp(event)
            }
            is SignUpEvent.NameChange -> {
                state = state.copy(name = event.name)
            }
            is SignUpEvent.EmailChange -> {
                state = state.copy(email = event.email)
            }
            is SignUpEvent.PasswordChange -> {
                state = state.copy(password = event.password)
            }
            is SignUpEvent.ShowPasswordChange -> {
                state = state.copy(showPassword = event.showPassword)
            }

            is SignUpEvent.FeatureInteraction -> sendFeatureInteractionEvent(event.featureName)
        }
    }

    private fun signUp(event: SignUpEvent.SignUp){
        viewModelScope.launch {
            try {
                if (event.email.replace(" ", "").isEmpty() || event.password.replace(" ", "").isEmpty() || event.name.replace(" ", "").isEmpty()) {
                    signUpFailure("Please fill in all fields")
                } else if (isConnected.value.not()) {
                    signUpFailure("Connectivity error: Please check your connection and try again.")
                } else {
                    val isAuthenticated = executeSignUp(event.email, event.password)
                    val isCreated = userUseCases.setUserInfo(event.name, event.email, getUserRandPic(), authUseCases.getCurrentUser()?.uid ?: "")

                    if (isAuthenticated && isCreated) {
                        event.onSuccess()
                    }
                }
                state = state.copy(isLoading = false)
            } catch (e: Exception) {
                Log.e("SignInViewModel", "Error during sign-up", e)
            }
        }
    }

    private suspend fun executeSignUp(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            var authenticated = false
            val result = authUseCases.executeSignUp(email, password)
            result.onSuccess {
                authenticated = true
            }.onFailure {
                signUpFailure(it.message ?: "An error occurred")
            }
            Log.d("SignUpViewModel", "executeSignUp: $authenticated")
            //authCheck()
            authenticated
        }
    }

    private fun signUpFailure(message: String) {
        state = state.copy(errSignUp = message)
    }

    private fun getUserRandPic(): String {
        val pictureLinks = arrayOf(
            "https://firebasestorage.googleapis.com/v0/b/restau-5dba7.appspot.com/o/profilePics%2Falien.png?alt=media&token=741eaac3-c4a5-4753-9293-9f40826dbc0d",
            "https://firebasestorage.googleapis.com/v0/b/restau-5dba7.appspot.com/o/profilePics%2Fant.png?alt=media&token=183f90eb-5ef3-4206-a863-738c64235cef",
            "https://firebasestorage.googleapis.com/v0/b/restau-5dba7.appspot.com/o/profilePics%2Fastronaut.png?alt=media&token=d4eb4a0f-0205-4d78-9edb-3fbcab8181de",
            "https://firebasestorage.googleapis.com/v0/b/restau-5dba7.appspot.com/o/profilePics%2Fbee.png?alt=media&token=7a934c3e-a0bd-4557-b64d-200aec680bf2",
            "https://firebasestorage.googleapis.com/v0/b/restau-5dba7.appspot.com/o/profilePics%2Fcat.png?alt=media&token=ec0c9822-0446-453d-92de-d67620be6008"
        )
        val rnds = (0..4).random()
        return pictureLinks[rnds]
    }

    private fun sendFeatureInteractionEvent(featureName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsUseCases.sendFeatureInteraction(featureName, "new_user")
        }
    }
}