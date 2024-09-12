package com.example.restau.domain.usecases

import android.icu.util.Calendar


class GetCurrentDay {

    operator fun invoke(): String {
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