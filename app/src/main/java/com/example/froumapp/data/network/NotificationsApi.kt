package com.example.froumapp.data.network

import com.example.froumapp.data.responses.MessageResponse
import com.example.froumapp.data.responses.NotificationResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header

interface NotificationsApi {

    @GET("/notification/")
    suspend fun getNotifications(
        @Header("Authorization") token: String
    ): NotificationResponse

    @DELETE("/notification/")
    suspend fun deleteNotifications(
        @Header("Authorization") token: String
    ): MessageResponse
}