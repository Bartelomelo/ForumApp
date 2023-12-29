package com.example.froumapp.ui.forum.categories.forums.threads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.ThreadForumRepository
import com.example.froumapp.data.responses.FollowResponse
import com.example.froumapp.data.responses.MessageResponse
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.data.responses.Type
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ForumThreadViewModel @Inject constructor(
    private val repository: ThreadForumRepository
): BaseViewModel(repository) {
    private val _threadResponse: MutableLiveData<Resource<ThreadResponse>> = MutableLiveData()
    private val _createdThreadResponse: MutableLiveData<Resource<ThreadResponseItem>> = MutableLiveData()
    private val _followResponse: MutableLiveData<Resource<FollowResponse>> = MutableLiveData()
    private val _messageResponse: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()

    val threadResponse: LiveData<Resource<ThreadResponse>>
        get() = _threadResponse
    val createdThreadResponse: LiveData<Resource<ThreadResponseItem>>
        get() = _createdThreadResponse
    val followResponse: LiveData<Resource<FollowResponse>>
        get() = _followResponse
    val messageResponse: LiveData<Resource<MessageResponse>>
        get() = _messageResponse

    fun getThreadsByForumId(forumId: String) = viewModelScope.launch {
        _threadResponse.value = repository.getThreadsByForumId(forumId)
    }

    fun addThread(token: String, forumId: String, threadTitle: String, threadDescription: String, userId: String) = viewModelScope.launch{
        _createdThreadResponse.value = repository.addThread(token, forumId, threadTitle, threadDescription, userId)
    }

    fun followForum(token: String, forumId: String, type: Int) = viewModelScope.launch {
        val user = Type(type)
        _followResponse.value = repository.followForum(token, forumId, user)
    }

    fun uploadThreadPicture(
        threadId: String,
        filename: String,
        file: File
    ) = viewModelScope.launch {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("threadId", threadId)
            .addFormDataPart("filename", filename)
            .addFormDataPart("file", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
            .build()
        _messageResponse.value = repository.uploadThreadPicture(requestBody)
    }
}