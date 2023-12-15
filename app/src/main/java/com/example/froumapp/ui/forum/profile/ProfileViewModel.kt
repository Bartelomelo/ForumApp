package com.example.froumapp.ui.forum.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.ProfileRepository
import com.example.froumapp.data.responses.MessageResponse
import com.example.froumapp.data.responses.UpdateResponse
import com.example.froumapp.data.responses.UpdateUser
import com.example.froumapp.data.responses.UploadBody
import com.example.froumapp.data.responses.User
import com.example.froumapp.ui.base.BaseViewModel
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
): BaseViewModel(repository) {

    private val _updateResponse: MutableLiveData<Resource<UpdateResponse>> = MutableLiveData()
    val updateResponse: LiveData<Resource<UpdateResponse>>
        get() = _updateResponse

    private val _user: MutableLiveData<Resource<User>> = MutableLiveData()
    val user: LiveData<Resource<User>>
        get() = _user

    private val _messageResponse: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()
    val messageResponse: LiveData<Resource<MessageResponse>>
        get() = _messageResponse

    fun getUser(userId: String) = viewModelScope.launch {
        _user.value = repository.getUser(userId)
    }

    fun updateUser(
        token: String,
        userId: String,
        userName: String,
        email: String,
        about: String,
        signature: String,
        userPicture: String
    ) = viewModelScope.launch {
        val user = UpdateUser(about, email, signature, userName, userId, userPicture)
        _updateResponse.value = repository.updateUser(token, userId, user)
    }

    fun uploadProfilePicture(
        username: String,
        filename: String,
        file: File
    ) = viewModelScope.launch {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("filename", filename)
            .addFormDataPart("file", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
            .build()
        _messageResponse.value = repository.uploadProfilePicture(requestBody)
    }
}