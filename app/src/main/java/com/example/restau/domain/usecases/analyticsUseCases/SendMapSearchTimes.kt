package com.example.restau.domain.usecases.analyticsUseCases

import com.example.restau.domain.repository.MapSearchTimesRepository
import com.google.firebase.Timestamp

class SendMapSearchTimes(
    private val mapSearchTimesRepository: MapSearchTimesRepository
) {

    suspend operator fun invoke() {
        mapSearchTimesRepository.sendMapSearchTimes(Timestamp.now())
    }

}