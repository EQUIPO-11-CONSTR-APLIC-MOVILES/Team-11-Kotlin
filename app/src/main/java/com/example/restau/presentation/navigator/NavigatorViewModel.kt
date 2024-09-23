package com.example.restau.presentation.navigator

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.AuthUseCases
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigatorViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    var selected by mutableIntStateOf(0)
        private set

    var showSplash by mutableStateOf(true)
        private set

    var currentUser = MutableStateFlow<FirebaseUser?>(null)

    fun onEvent(event: NavigatorEvent) {
        when (event) {
            is NavigatorEvent.SelectedChange -> {
                selected = event.selected
            }

            is NavigatorEvent.AuthCheck -> {
                viewModelScope.launch {
                    authCheck()
                }
            }
        }
    }

    private suspend fun getCurrentUser(): FirebaseUser? {
        return authUseCases.getCurrentUser()
    }

    suspend fun authCheck() {
        currentUser.value = getCurrentUser()
        Log.d("NavigatorViewModel", "authCheck: ${currentUser.value}")
        showSplash = false
    }
}



