package com.example.froumapp.data.network

import com.example.froumapp.data.responses.Post
import com.example.froumapp.data.responses.UserId
import com.example.froumapp.data.responses.VoteResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostApi {

    @FormUrlEncoded
    @POST("post/")
    suspend fun addPost(
        @Header("Authorization") token: String,
        @Field("authorId") userId: String,
        @Field("comment") comment: String,
        @Field("threadId") threadId: String
    ): Post

    @GET("post/votes/{postId}")
    suspend fun getVotes(
        @Path("postId") postId: String
    ): Int

    @PUT("post/{postId}/vote/{type}")
    suspend fun votePost(
        @Header("Authorization") token: String,
        @Path("postId") postId: String,
        @Path("type") type: String,
        @Body userId: UserId
    ): VoteResponse
}