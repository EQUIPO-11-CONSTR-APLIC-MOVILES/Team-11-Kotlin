package com.example.restau.domain.usecases

import com.example.restau.domain.model.Restaurant
import java.util.Date

class GetIsNewRestaurantArray {

    operator fun invoke(restaurants: List<Restaurant>): List<Boolean> {
        return restaurants.map { restaurant ->
            val openingDateDays = restaurant.openingDate.toDate().time/ 86400000L
            val currentDays = Date().time/ 86400000L
            val differenceDays = currentDays - openingDateDays
            differenceDays < 30L
        }
    }

}