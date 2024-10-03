package com.example.restau.presentation.signin

sealed class SignInEvent {
    data class SignIn(val email: String, val password: String, val onSuccess: ()-> Unit, val authCheck: suspend ()-> Unit ): SignInEvent()
    data class EmailChange(val email: String): SignInEvent()
    data class PasswordChange(val password: String): SignInEvent()
    data class ShowPasswordChange(val showPassword: Boolean): SignInEvent()
    data class FeatureInteraction(val featureName: String): SignInEvent()
}