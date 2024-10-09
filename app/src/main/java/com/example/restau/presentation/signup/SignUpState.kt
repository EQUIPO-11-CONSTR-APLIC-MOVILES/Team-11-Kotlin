package com.example.restau.presentation.signup

data class SignUpState (
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val errSignUp: String = "",
    val isLoading: Boolean = false
)