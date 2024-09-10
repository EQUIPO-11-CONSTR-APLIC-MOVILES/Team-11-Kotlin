package com.example.restau.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.restau.domain.repository.RecentsRepository
import com.example.restau.utils.Constants
import com.example.restau.utils.Constants.DATASTORE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecentsRepositoryImpl(
    private val context: Context
): RecentsRepository {

    override suspend fun saveRecentsEntry(recents: Set<String>) {
        context.datastore.edit { settings ->
            settings[PreferenceKeys.RECENTS_ENTRY] = recents
        }
    }

    override fun readRecentsEntry(): Flow<Set<String>> {
        return context.datastore.data.map { preferences ->
            preferences[PreferenceKeys.RECENTS_ENTRY] ?: emptySet()
        }
    }
}

private val preferences = preferencesDataStore(name = DATASTORE_NAME)

val Context.datastore: DataStore<Preferences> by preferences

private object PreferenceKeys {
    val RECENTS_ENTRY = stringSetPreferencesKey(Constants.RECENTS_ENTRY)
}