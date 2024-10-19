package com.example.restau.domain.usecases.restaurantUseCases

import android.annotation.SuppressLint
import android.icu.util.Calendar
import com.example.restau.domain.repository.RestaurantsRepository
import java.text.SimpleDateFormat
import java.util.Date

class IsOpen(
    private val restaurantsRepository: RestaurantsRepository
) {
    suspend operator fun invoke(restaurantID: String): Boolean {
        val day = getDay()
        val time = getTime()
        return restaurantsRepository.isOpen(day, time, restaurantID)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTime(): Int {
        val currentDate = Date()
        val time = SimpleDateFormat("HHmm").format(currentDate)
        return time.toInt()
    }

    private fun getDay(): String {
        val calendar = Calendar.getInstance()
        val dayNum = calendar.get(Calendar.DAY_OF_WEEK)
        val day = when (dayNum) {
            Calendar.MONDAY -> "monday"
            Calendar.TUESDAY -> "tuesday"
            Calendar.WEDNESDAY -> "wednesday"
            Calendar.THURSDAY -> "thursday"
            Calendar.FRIDAY -> "friday"
            Calendar.SATURDAY -> "saturday"
            Calendar.SUNDAY -> "sunday"
            else -> "E"
        }
        return day
    }
}