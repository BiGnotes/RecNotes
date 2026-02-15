package com.recnotes.ui.screens.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val API_KEY = stringPreferencesKey("api_key")
        private val MODEL_NAME = stringPreferencesKey("model_name")
    }

    fun getApiKey(): Flow<String> = context.dataStore.data.map { preferences ->
        preferences[API_KEY] ?: ""
    }

    fun getModelName(): Flow<String> = context.dataStore.data.map { preferences ->
        preferences[MODEL_NAME] ?: "deepseek-ai/DeepSeek-R1-0528-Qwen3-8B"
    }

    suspend fun saveApiKey(key: String) {
        context.dataStore.edit { preferences ->
            preferences[API_KEY] = key
        }
    }

    suspend fun saveModelName(model: String) {
        context.dataStore.edit { preferences ->
            preferences[MODEL_NAME] = model
        }
    }
}
