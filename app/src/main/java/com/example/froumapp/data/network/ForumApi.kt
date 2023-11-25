package com.example.froumapp.data.network

import com.example.froumapp.data.responses.ForumResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ForumApi {
    @GET("forum/categoryId/{id}")
    suspend fun getForums(@Path("id") categoryId: String): ForumResponse
}