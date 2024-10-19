package com.example.restau.domain.usecases.locationUseCases

import android.content.Intent
import android.net.Uri

class LaunchMaps {
    operator fun invoke(lat: Double, lon: Double, name: String, place: String):Intent {
        val gmmIntentURI = Uri.parse("geo:$lat,$lon?q=$name $place")
        return Intent(Intent.ACTION_VIEW, gmmIntentURI).apply {
            setPackage("com.google.android.apps.maps")
        }
    }
}