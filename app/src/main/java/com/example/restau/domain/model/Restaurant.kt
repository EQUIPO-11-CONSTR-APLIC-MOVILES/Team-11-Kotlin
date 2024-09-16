package com.example.restau.domain.model

import com.google.firebase.Timestamp
import java.util.Date

data class Restaurant(
    val documentId: String = "",
    val name: String = "",
    val placeName: String = "",
    val openingDate: Timestamp = Timestamp(Date()),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val averageRating: Double = 0.0,
    val imageUrl: String = "",
    val categories: List<String> = emptyList(),
    val schedule: Map<String, Map<String, Double>> = emptyMap()
)
