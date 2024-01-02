package com.example.froumapp.ui.forum.thread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.ThreadRepository
import com.example.froumapp.data.responses.DeleteResponse
import com.example.froumapp.data.responses.FollowResponse
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.data.responses.Type
import com.example.froumapp.data.responses.UpdateThread
import com.example.froumapp.data.responses.UpdateThreadResponse
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val repository: ThreadRepository
): BaseViewModel(repository) {
    private val _threadResponseItem: MutableLiveData<Resource<ThreadResponseItem>> = MutableLiveData()
    private val _followUnfollowMessage: MutableLiveData<Resource<FollowResponse>> = MutableLiveData()
    private val _deleteResponse: MutableLiveData<Resource<DeleteResponse>> = MutableLiveData()
    private val _updateThreadResponse: MutableLiveData<Resource<UpdateThreadResponse>> = MutableLiveData()
    val threadResponseItem: LiveData<Resource<ThreadResponseItem>>
        get() = _threadResponseItem
    val followUnfollowMessage: LiveData<Resource<FollowResponse>>
        get() = _followUnfollowMessage
    val deleteResponse: LiveData<Resource<DeleteResponse>>
        get() = _deleteResponse
    val updateThreadResponse: LiveData<Resource<UpdateThreadResponse>>
        get() = _updateThreadResponse
    fun getThreadById(threadId: String) = viewModelScope.launch {
        _threadResponseItem.value = repository.getThreadById(threadId)
    }
    fun followUnfollowThread(token: String, threadId: String, type: Int) = viewModelScope.launch {
        val user =  Type(type)
        _followUnfollowMessage.value = repository.followUnfollowThread(token, threadId, user)
    }
    fun deleteThread(token: String, threadId: String) = viewModelScope.launch {
        _deleteResponse.value = repository.deleteThread(token, threadId)
    }
    fun updateThread(token: String, threadId: String, description: String) = viewModelScope.launch {
        val updateBody = UpdateThread(description)
        _updateThreadResponse.value = repository.updateThread(token, threadId, updateBody)
    }
}