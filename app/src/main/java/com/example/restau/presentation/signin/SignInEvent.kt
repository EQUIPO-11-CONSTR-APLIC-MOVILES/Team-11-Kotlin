package com.example.restau.presentation.signin

import androidx.navigation.NavController
import kotlin.reflect.KFunction1

sealed class SignInEvent {
    data class SignIn(val email: String, val password: String, val navController: NavController): SignInEvent()
}