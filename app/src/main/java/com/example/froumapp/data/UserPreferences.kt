package com.example.froumapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    context: Context
) {
    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ForumAppDataStore")
    }


    private val applicationContext = context.applicationContext

    val authToken: Flow<String?>
        get() = applicationContext.dataStore.data.map {preferences ->
            preferences[KEY_AUTH]
        }

    suspend fun saveAuthToken(authToken: String) {
        applicationContext.dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    suspend fun removeToken() {
        applicationContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}