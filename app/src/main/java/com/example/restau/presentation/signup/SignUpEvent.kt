package com.example.restau.presentation.signup

sealed class SignUpEvent {
    data class SignUp(val name: String, val email: String, val password: String, val onSuccess: ()-> Unit): SignUpEvent()
    data class NameChange(val name: String): SignUpEvent()
    data class EmailChange(val email: String): SignUpEvent()
    data class PasswordChange(val password: String): SignUpEvent()
    data class ShowPasswordChange(val showPassword: Boolean): SignUpEvent()
    data class FeatureInteraction(val featureName: String) : SignUpEvent()
}