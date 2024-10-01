package com.example.restau.presentation.preferences

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    var state by mutableStateOf(PreferencesState())

    fun onEvent(event: PreferencesEvent) {
        when (event) {
            is PreferencesEvent.StartScroll -> {
                val temp = state.scrollStates.toMutableList()
                temp.add(event.index, event.scrollState)
                state = state.copy(scrollStates = temp)
            }

            is PreferencesEvent.VerticalScroll -> {
                state = state.copy(verticalScrollState = event.scrollState)
            }

            is PreferencesEvent.SelectTag -> {
                val temp = state.selectedTags.toMutableList()
                if (temp.contains(event.tag)) {
                    temp.remove(event.tag)
                } else if (temp.size < 15) {
                    temp.add(event.tag)
                }
                state = state.copy(selectedTags = temp)
            }

            is PreferencesEvent.SaveTags -> {
                saveTags(event.onSuccess, event.authCheck)
            }
        }
    }

    private fun saveTags(onSuccess: () -> Unit, authCheck: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userUseCases.getUserObject()
            userUseCases.saveTags(state.selectedTags, user)
            authCheck()
        }
        onSuccess()
    }
}
