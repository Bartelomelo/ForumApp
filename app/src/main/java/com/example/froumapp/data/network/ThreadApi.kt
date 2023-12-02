package com.example.froumapp.data.network

import com.example.froumapp.data.responses.FollowResponse
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.data.responses.UserId
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ThreadApi {

    @GET("thread/")
    suspend fun getLastThreads(): ThreadResponse

    @FormUrlEncoded
    @POST("thread/")
    suspend fun addThread(
        @Header("Authorization") token: String,
        @Field("forumId") forumId: String,
        @Field("title") threadTitle: String,
        @Field("description") threadDescription: String,
        @Field("authorId") userId : String
    ): ThreadResponseItem

    @GET("thread/{threadId}")
    suspend fun getThreadById(@Path("threadId") threadId: String): ThreadResponseItem

    @GET("thread/forumId/{forumId}")
    suspend fun getThreadsByForumId(@Path("forumId") forumId: String): ThreadResponse

    @PUT("thread/{threadId}/follow/{type}")
    suspend fun followUnfollowThread(
        @Header("Authorization") token: String,
        @Path("threadId") threadId: String,
        @Path("type") type: String,
        @Body user: UserId
    ): FollowResponse
}