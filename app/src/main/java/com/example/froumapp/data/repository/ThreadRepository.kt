package com.example.froumapp.data.repository

import com.example.froumapp.data.network.ThreadApi
import javax.inject.Inject

class ThreadRepository @Inject constructor(
    private val api: ThreadApi
): BaseRepository() {
    suspend fun getThreadById(threadId: String) = safeApiCall {
        api.getThreadById(threadId)
    }
}