package com.example.froumapp.data.repository

import com.example.froumapp.data.network.PostApi
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val api: PostApi
): BaseRepository() {
    suspend fun addPost(token: String, userId: String, comment: String, threadId: String) = safeApiCall {
        api.addPost(token, userId, comment, threadId)
    }
}