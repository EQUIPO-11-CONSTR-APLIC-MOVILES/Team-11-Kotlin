package com.example.restau.domain.repository

import com.example.restau.domain.model.ScreenTimeEvent

interface ScreenTimeEventsRepository {

    suspend fun sendScreenTimeEvent(screenTimeEvent: ScreenTimeEvent)

}