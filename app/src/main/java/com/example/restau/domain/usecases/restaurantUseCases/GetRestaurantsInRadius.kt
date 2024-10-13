package com.example.restau.domain.usecases.restaurantUseCases

import android.location.Location
import com.example.restau.domain.model.Restaurant
import com.google.android.gms.maps.model.LatLng

class GetRestaurantsInRadius {

    operator fun invoke(restaurants: List<Restaurant>, currentLocation: LatLng, radius: Double): List<Boolean> {
        val response = restaurants.map {
            val results = FloatArray(1)
            Location.distanceBetween(
                currentLocation.latitude,
                currentLocation.longitude,
                it.latitude,
                it.longitude,
                results
            )
            results[0] < radius
        }
        return response
    }

}