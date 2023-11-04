package com.example.froumapp.data.network

interface UserApi {
    suspend fun logout(): String
}