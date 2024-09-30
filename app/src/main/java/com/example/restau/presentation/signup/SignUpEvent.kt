package com.example.restau.presentation.signup

sealed class SignUpEvent {
    data class SignUp(val name: String, val email: String, val password: String, val onSuccess: ()-> Unit, val authCheck: suspend ()-> Unit ): SignUpEvent()
}