package com.example.restau.presentation.signin

data class SignInState (
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val errSignIn: String = "",
    val isLoading: Boolean = false
)