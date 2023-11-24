package com.example.froumapp.data.repository

import com.example.froumapp.data.network.ForumApi
import javax.inject.Inject

class ForumRepository @Inject constructor(
    private val api: ForumApi
): BaseRepository(){
    suspend fun getForums(id: String) = safeApiCall {
        api.getForums(id)
    }
}