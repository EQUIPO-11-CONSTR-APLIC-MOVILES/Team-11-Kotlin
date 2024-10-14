package com.example.restau.domain.model

import com.google.firebase.Timestamp
import java.util.Date

data class Review(
    val documentId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val description: String = "",
    val rating: Int = 0,
    val restaurantId: String = "",
    val authorPFP: String = "",
    val date: Timestamp = Timestamp(Date())
)
