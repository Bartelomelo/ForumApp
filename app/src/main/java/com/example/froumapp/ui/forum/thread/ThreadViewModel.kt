package com.example.froumapp.ui.forum.thread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.ThreadRepository
import com.example.froumapp.data.responses.FollowResponse
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.data.responses.Type
import com.example.froumapp.data.responses.UserId
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
    val threadResponseItem: LiveData<Resource<ThreadResponseItem>>
        get() = _threadResponseItem
    val followUnfollowMessage: LiveData<Resource<FollowResponse>>
        get() = _followUnfollowMessage
    fun getThreadById(threadId: String) = viewModelScope.launch {
        _threadResponseItem.value = repository.getThreadById(threadId)
    }
    fun followUnfollowThread(token: String, threadId: String, type: Int) = viewModelScope.launch {
        val user =  Type(type)
        _followUnfollowMessage.value = repository.followUnfollowThread(token, threadId, user)
    }
}