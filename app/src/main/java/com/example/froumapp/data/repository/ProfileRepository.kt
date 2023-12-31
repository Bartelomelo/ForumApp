package com.example.froumapp.data.repository

import com.example.froumapp.data.network.UserApi
import com.example.froumapp.data.responses.UpdateUser
import com.example.froumapp.data.responses.UploadBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val api: UserApi,
//    private val preferences: UserPreferences
): BaseRepository() {

    suspend fun getUser(userId: String) = safeApiCall {
        api.getUser(userId)
    }

    suspend fun updateUser(token: String, userId: String, user: UpdateUser) = safeApiCall {
        api.updateUser(token, userId, user)
    }

    suspend fun uploadProfilePicture(requestBody: RequestBody) = safeApiCall {
        api.uploadProfilePicture(requestBody)
    }
}