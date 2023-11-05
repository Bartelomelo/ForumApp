package com.example.froumapp.data.repository

import com.example.froumapp.data.UserPreferences
import com.example.froumapp.data.network.AuthApi

class AuthRepository(
    private val api: AuthApi,
    private val preferences: UserPreferences
) : BaseRepository() {
    suspend fun login(
        userName: String,
        password: String
    ) = safeApiCall {
        api.login(userName, password)
    }

    suspend fun saveUserCredentials(token: String, userId: String) {
        preferences.saveUserCredentials(token, userId)
    }
}