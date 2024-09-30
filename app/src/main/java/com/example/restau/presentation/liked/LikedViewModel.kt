package com.example.restau.presentation.liked

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.AnalyticsUseCases
import com.example.restau.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LikedViewModel @Inject constructor(
    private val analyticsUseCases: AnalyticsUseCases,
    private val userUseCases: UserUseCases
): ViewModel() {

    private var currentUser by mutableStateOf(User())

    private var startTime by mutableStateOf(Date())

    init {
        updateUser()
    }

    private fun updateUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userUseCases.getUserObject()
            currentUser = user
        }
    }

    private fun startTimer() {
        startTime = Date()
    }

    private fun sendEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val miliDifference = Date().time - startTime.time
            val timer = TimeUnit.MILLISECONDS.toSeconds(miliDifference)
            analyticsUseCases.sendScreenTimeEvent("liked_screen", timer, currentUser.documentId)
        }
    }

    fun onEvent(event: LikedEvent) {
        when(event) {
            is LikedEvent.ScreenOpened -> {
                startTimer()
            }
            is LikedEvent.ScreenClosed -> {
                sendEvent()
            }
        }
    }


}