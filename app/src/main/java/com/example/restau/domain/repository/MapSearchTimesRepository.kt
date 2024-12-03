package com.example.restau.domain.repository

import com.google.firebase.Timestamp

interface MapSearchTimesRepository {

    suspend fun sendMapSearchTimes(time: Timestamp)

}