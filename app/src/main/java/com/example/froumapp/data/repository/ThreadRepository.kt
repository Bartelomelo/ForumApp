package com.example.froumapp.data.repository

import com.example.froumapp.data.network.ThreadApi
import com.example.froumapp.data.responses.Type
import com.example.froumapp.data.responses.UpdateThread
import javax.inject.Inject

class ThreadRepository @Inject constructor(
    private val api: ThreadApi
): BaseRepository() {
    suspend fun getThreadById(threadId: String) = safeApiCall {
        api.getThreadById(threadId)
    }

    suspend fun followUnfollowThread(token: String, threadId: String, user: Type) = safeApiCall {
        api.followUnfollowThread(token, threadId, user)
    }

    suspend fun deleteThread(token: String, threadId: String) = safeApiCall {
        api.deleteThread(token, threadId)
    }

    suspend fun updateThread(token: String, threadId: String, updateBody: UpdateThread) = safeApiCall {
        api.updateThread(token, threadId, updateBody)
    }
}