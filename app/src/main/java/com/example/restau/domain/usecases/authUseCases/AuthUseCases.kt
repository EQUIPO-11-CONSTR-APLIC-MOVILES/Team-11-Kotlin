package com.example.restau.domain.usecases.authUseCases

class AuthUseCases (
    val executeSignIn: ExecuteSignIn,
    val getCurrentUser: GetCurrentUser,
    val executeSignUp: ExecuteSignUp,
    val executeLogOut: ExecuteLogOut
)