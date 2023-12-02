package com.example.froumapp.data.network

import com.example.froumapp.data.responses.Post
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface PostApi {

    @FormUrlEncoded
    @POST("post/")
    suspend fun addPost(
        @Header("Authorization") token: String,
        @Field("authorId") userId: String,
        @Field("comment") comment: String,
        @Field("threadId") threadId: String
    ): Post
}