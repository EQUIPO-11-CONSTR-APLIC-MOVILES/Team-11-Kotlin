package com.example.restau.presentation.preferences

import androidx.compose.foundation.ScrollState

data class PreferencesState (
    val scrollStates: List<ScrollState> = emptyList(),
    val selectedTags: List<String> = emptyList(),
    val verticalScrollState: ScrollState? = null
)