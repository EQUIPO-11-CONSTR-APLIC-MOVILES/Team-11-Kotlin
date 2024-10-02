package com.example.restau.presentation.preferences

sealed class PreferencesEvent {
    data class SelectTag(val tag: String) : PreferencesEvent()
    data class SaveTags(val tags: List<String>, val onSuccess: () -> Unit, val authCheck: suspend ()  -> Unit ) : PreferencesEvent()
    class GetTags: PreferencesEvent()
}