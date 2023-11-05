package com.example.froumapp.data

import android.content.Context
import android.util.Log
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
        private val USER_ID = stringPreferencesKey("user_id")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ForumAppDataStore")
    }


    private val applicationContext = context.applicationContext

    val authToken: Flow<String?>
        get() = applicationContext.dataStore.data.map {preferences ->
            preferences[KEY_AUTH]
        }
    val userId: Flow<String?> = applicationContext.dataStore.data.map { preferences ->
            preferences[USER_ID]
        }


    suspend fun saveUserCredentials(authToken: String, userId: String) {
        applicationContext.dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
            preferences[USER_ID] = userId
        }
    }


    suspend fun removeUserCredentials() {
        applicationContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}