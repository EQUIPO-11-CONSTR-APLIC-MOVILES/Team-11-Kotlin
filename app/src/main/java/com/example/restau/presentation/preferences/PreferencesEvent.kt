package com.example.restau.presentation.preferences

import androidx.compose.foundation.ScrollState

sealed class PreferencesEvent {
    data class StartScroll(val index: Int, val scrollState: ScrollState) : PreferencesEvent()
    data class SelectTag(val tag: String) : PreferencesEvent()
    data class VerticalScroll(val scrollState: ScrollState) : PreferencesEvent()
    data class SaveTags(val tags: List<String>, val onSuccess: () -> Unit, val authCheck: suspend ()  -> Unit ) : PreferencesEvent()
}