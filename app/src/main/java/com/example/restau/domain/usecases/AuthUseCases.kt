package com.example.restau.domain.usecases

import com.example.restau.presentation.signup.SignUpEvent

class AuthUseCases (
    val executeSignIn: ExecuteSignIn,
    val getCurrentUser: GetCurrentUser,
    val executeSignUp: ExecuteSignUp,
)