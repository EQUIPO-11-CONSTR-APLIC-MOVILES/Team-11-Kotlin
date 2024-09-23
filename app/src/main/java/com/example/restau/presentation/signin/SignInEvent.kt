package com.example.restau.presentation.signin

sealed class SignInEvent {
    data class SignIn(val email: String, val password: String, val onSuccess: ()-> Unit, val authCheck: suspend ()-> Unit ): SignInEvent()
}