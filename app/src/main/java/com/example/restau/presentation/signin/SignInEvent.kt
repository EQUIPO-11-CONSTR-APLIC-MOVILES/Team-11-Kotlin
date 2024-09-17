package com.example.restau.presentation.signin

import androidx.navigation.NavController

sealed class SignInEvent {
    data class SignIn(val email: String, val password: String, val navController: NavController): SignInEvent()
}