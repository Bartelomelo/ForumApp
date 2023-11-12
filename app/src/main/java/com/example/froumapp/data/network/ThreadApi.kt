package com.example.froumapp.data.network

import com.example.froumapp.data.responses.ThreadResponse
import retrofit2.http.GET

interface ThreadApi {

    @GET("thread/")
    suspend fun getLastThreads(): ThreadResponse
}