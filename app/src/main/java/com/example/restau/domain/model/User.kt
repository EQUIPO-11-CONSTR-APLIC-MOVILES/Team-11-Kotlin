package com.example.restau.domain.model

data class User (
    val documentId: String = "",
    val email: String = "",
    val likes: List<String> = emptyList(),
    val name: String = "",
    val preferences: List<String> = emptyList(),
    val profilePic: String = ""
)