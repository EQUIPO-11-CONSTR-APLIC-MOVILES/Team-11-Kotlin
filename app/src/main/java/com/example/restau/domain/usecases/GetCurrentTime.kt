package com.example.restau.domain.usecases

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

class GetCurrentTime {

    @SuppressLint("SimpleDateFormat")
    operator fun invoke(): Int {
        val time = SimpleDateFormat("HHmm").format(Date())
        return time.toInt()
    }

}