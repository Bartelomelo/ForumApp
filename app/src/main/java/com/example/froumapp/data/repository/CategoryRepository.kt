package com.example.froumapp.data.repository

import com.example.froumapp.data.network.CategoryApi
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val api: CategoryApi
): BaseRepository() {
    suspend fun getCategories() = safeApiCall {
        api.getCategories()
    }
}