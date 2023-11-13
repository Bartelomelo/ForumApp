package com.example.froumapp.data.network

import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.data.responses.ThreadResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ThreadApi {

    @GET("thread/")
    suspend fun getLastThreads(): ThreadResponse

    @GET("thread/{threadId}")
    suspend fun getThreadById(@Path("threadId")threadId: String): ThreadResponseItem
}