package com.example.restau.domain.repository

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getLocationUpdates(): Flow<LatLng?>

    fun getCurrentLocation(): Flow<LatLng?>

}