package com.example.froumapp.data.repository

import com.example.froumapp.data.network.NotificationsApi
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val api: NotificationsApi
) : BaseRepository() {

    suspend fun getNotifications(token: String) = safeApiCall {
        api.getNotifications(token)
    }
}