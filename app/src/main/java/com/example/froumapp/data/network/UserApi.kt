package com.example.froumapp.data.network

import com.example.froumapp.data.responses.UpdateResponse
import com.example.froumapp.data.responses.UpdateUser
import com.example.froumapp.data.responses.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {
    @GET("user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User

    @PUT("user/{userId}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body user: UpdateUser
    ): UpdateResponse
}