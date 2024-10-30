package com.example.restau.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.repository.RecentsRepository
import com.example.restau.utils.Constants
import com.example.restau.utils.Constants.DATASTORE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecentsRepositoryImpl(
    private val context: Context,
    private val gson: Gson = Gson()
): RecentsRepository {

    override suspend fun saveRecentsEntry(recents: Set<Restaurant>) {
        val json = gson.toJson(recents)
        context.datastore.edit { settings ->
            settings[PreferenceKeys.RECENTS_ENTRY] = json
        }
    }

    override fun readRecentsEntry(): Flow<Set<Restaurant>> {
        return context.datastore.data.map { preferences ->
            val json = preferences[PreferenceKeys.RECENTS_ENTRY] ?: ""
            if (json.isNotEmpty()) {
                val type = object : TypeToken<Set<Restaurant>>() {}.type
                gson.fromJson(json, type)
            } else {
                emptySet()
            }
        }
    }
}

private val preferences = preferencesDataStore(name = DATASTORE_NAME)

val Context.datastore: DataStore<Preferences> by preferences

private object PreferenceKeys {
    val RECENTS_ENTRY = stringPreferencesKey(Constants.RECENTS_ENTRY)
}