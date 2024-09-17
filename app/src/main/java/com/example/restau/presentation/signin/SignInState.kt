package com.example.restau.presentation.signin

data class SignInState (
    val errSignIn: Boolean = false,
    val noConnectivity : Boolean = false
)