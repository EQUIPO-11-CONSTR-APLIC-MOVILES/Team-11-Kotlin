package com.example.restau.domain.usecases

import com.example.restau.domain.model.ScreenTimeEvent
import com.example.restau.domain.repository.ScreenTimeEventsRepository

class SendScreenTimeEvent(
    private val screenTimeEventsRepository: ScreenTimeEventsRepository
) {

    suspend operator fun invoke(screen: String, duration: Long, user_id: String) {
        val screenTimeEvent = ScreenTimeEvent(
            screen = screen,
            time_seconds = duration,
            user_id = user_id
        )
        screenTimeEventsRepository.sendScreenTimeEvent(screenTimeEvent)
    }

}