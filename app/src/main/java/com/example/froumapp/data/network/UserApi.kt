package com.example.froumapp.data.network

import com.example.froumapp.data.responses.LoginResponse
import com.example.froumapp.data.responses.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    suspend fun logout(): String

    @GET("user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User
}