package com.example.froumapp.data.repository

import com.example.froumapp.data.network.PostApi
import com.example.froumapp.data.responses.UserId
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val api: PostApi
): BaseRepository() {
    suspend fun addPost(token: String, userId: String, comment: String, threadId: String) = safeApiCall {
        api.addPost(token, userId, comment, threadId)
    }
    suspend fun votePost(token: String, postId: String, type: String, userId: UserId) = safeApiCall {
        api.votePost(token, postId, type, userId)
    }

    suspend fun getVotes(postId: String) = safeApiCall {
        api.getVotes(postId)
    }
}
