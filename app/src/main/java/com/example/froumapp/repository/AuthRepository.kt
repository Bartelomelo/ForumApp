package com.example.froumapp.repository

import com.example.froumapp.network.AuthApi

class AuthRepository(
    private val api: AuthApi
) : BaseRepository() {
    suspend fun login(
        userName: String,
        password: String
    ) = safeApiCall {
        api.login(userName, password)
    }
}