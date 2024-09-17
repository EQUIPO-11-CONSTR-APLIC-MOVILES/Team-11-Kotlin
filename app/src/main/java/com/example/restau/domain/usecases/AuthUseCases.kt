package com.example.restau.domain.usecases

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AuthUseCases (
    val executeSignIn: ExecuteSignIn,
    val getCurrentUser: GetCurrentUser
){
    companion object {
        var isSignedIn: Boolean by mutableStateOf(value = false)
        var errSignIn: Boolean by mutableStateOf(value = false)
    }
}
