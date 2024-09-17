package com.example.restau.presentation.signin

import com.example.restau.domain.model.User

data class SignInState (
    val email: String = "",
    val password: String = "",
    val autheticated: Boolean = false,
    val error: Boolean = false
)