package com.example.restau.presentation.signin

data class SignInState (
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val errSignIn: Boolean = false,
    val noConnectivity : Boolean = false
)