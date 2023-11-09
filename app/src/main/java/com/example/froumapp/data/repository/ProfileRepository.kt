package com.example.froumapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.froumapp.data.UserPreferences
import com.example.froumapp.data.network.UserApi
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val api: UserApi,
    private val preferences: UserPreferences
): BaseRepository() {

    suspend fun getUser(userId: String) = safeApiCall {
        api.getUser(userId)
    }
}