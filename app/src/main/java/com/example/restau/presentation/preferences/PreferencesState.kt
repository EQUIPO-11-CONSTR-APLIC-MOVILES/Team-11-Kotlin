package com.example.restau.presentation.preferences

data class PreferencesState (
    val selectedTags: List<String> = emptyList(),
    val tags: Map<String, Pair<Int, List<String>>> = emptyMap(),
)