package com.example.restau.domain.usecases

import androidx.compose.foundation.lazy.items
import com.example.restau.domain.model.Restaurant
import java.util.Date

class GetFilterRestaurantsByNameAndCategories {

    operator fun invoke(restaurants: List<Restaurant>, restaurantName: String): List<Restaurant> {
        return restaurants.filter {
            it.name.contains(restaurantName, ignoreCase = true) ||
                    it.categories.any { category ->
                        category.contains(restaurantName, ignoreCase = true)
                    }
        }
    }

}