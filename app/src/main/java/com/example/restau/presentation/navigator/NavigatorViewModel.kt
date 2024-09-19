package com.example.restau.presentation.navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.AuthUseCases
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NavigatorViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {


    var selected by mutableIntStateOf(0)
        private set

    var showSplash by mutableStateOf(true)
        private set

    var currentUser: StateFlow<FirebaseUser?> = MutableStateFlow(null).asStateFlow()
        private set

    var isSignedIn by mutableStateOf(false)
        private set

    fun onEvent(event: NavigatorEvent) {
        when (event) {
            is NavigatorEvent.SelectedChange -> {
                selected = event.selected
            }

            is NavigatorEvent.AuthCheck -> {
                authCheck()
            }
        }
    }

    private suspend fun getCurrentUser(): StateFlow<FirebaseUser?> {
        return withContext(Dispatchers.IO) {
            authUseCases.getCurrentUser()
        }
    }

    private fun authCheck() {
        viewModelScope.launch {
            isSignedIn = getCurrentUser().value != null
            currentUser = getCurrentUser()
            showSplash = false
        }
    }
}



