package com.example.restau.domain.model

import com.google.firebase.Timestamp
import java.util.Date

data class ScreenTimeEvent(
    val date: Timestamp = Timestamp(Date()),
    val screen: String = "",
    val time_seconds: Long = 0L,
    val user_id: String = ""
)