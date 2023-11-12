package com.example.froumapp.data.repository

import com.example.froumapp.data.network.ThreadApi
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: ThreadApi
) : BaseRepository() {

    suspend fun getLastThreads() = safeApiCall {
        api.getLastThreads()
    }

}