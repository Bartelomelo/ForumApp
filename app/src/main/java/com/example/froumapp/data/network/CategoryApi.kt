package com.example.froumapp.data.network

import com.example.froumapp.data.responses.CategoryResponse
import retrofit2.http.GET

interface CategoryApi {
    @GET("category/")
    suspend fun getCategories(): CategoryResponse
}