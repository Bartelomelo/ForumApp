package com.example.froumapp.data.repository

import com.example.froumapp.data.network.ThreadApi
import javax.inject.Inject

class ThreadForumRepository @Inject constructor(
    private val api: ThreadApi
): BaseRepository() {
    suspend fun getThreadsByForumId(forumId: String) = safeApiCall {
        api.getThreadsByForumId(forumId)
    }
}