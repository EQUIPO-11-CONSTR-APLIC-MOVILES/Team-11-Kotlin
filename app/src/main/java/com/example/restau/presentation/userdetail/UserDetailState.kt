package com.example.restau.presentation.userdetail

import com.example.restau.domain.model.User

data class UserDetailState(
    val user: User? = null,
    val isUserUpdatedSuccessfully: Boolean = false,
    val isReviewAuthorUpdatedSuccessfully: Boolean = false,
    val isLogOut: Boolean = false
)