package com.example.restau.presentation.preferences

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.TagsUseCases
import com.example.restau.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val tagsUseCases: TagsUseCases
) : ViewModel() {

    var state by mutableStateOf(PreferencesState())

    fun onEvent(event: PreferencesEvent) {
        when (event) {

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

            is PreferencesEvent.GetTags -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state = state.copy( tags = getTags())
                }
            }
        }
    }

    private suspend fun getTags(): Map<String, Pair<Int, List<String>>> {
    return tagsUseCases.getTags()
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
