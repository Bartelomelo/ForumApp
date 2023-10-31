package com.example.froumapp.network

import com.example.froumapp.responses.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("")
    suspend fun login(
        @Field("userName") email: String,
        @Field("password") password: String
    ) : LoginResponse
}