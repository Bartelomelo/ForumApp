package com.example.froumapp.data.repository

import com.example.froumapp.data.network.ThreadApi
import com.example.froumapp.data.responses.UserId
import javax.inject.Inject

class ThreadRepository @Inject constructor(
    private val api: ThreadApi
): BaseRepository() {
    suspend fun getThreadById(threadId: String) = safeApiCall {
        api.getThreadById(threadId)
    }

    suspend fun followUnfollowThread(token: String, threadId: String, type: String, user: UserId) = safeApiCall {
        api.followUnfollowThread(token, threadId, type, user)
    }
}