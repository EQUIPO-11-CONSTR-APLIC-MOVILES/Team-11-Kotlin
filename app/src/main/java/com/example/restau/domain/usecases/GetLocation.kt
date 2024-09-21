package com.example.restau.domain.usecases

import com.example.restau.domain.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

class GetLocation(
    private val locationRepository: LocationRepository
) {

    operator fun invoke(): Flow<LatLng?> = locationRepository.getLocationUpdates()

}