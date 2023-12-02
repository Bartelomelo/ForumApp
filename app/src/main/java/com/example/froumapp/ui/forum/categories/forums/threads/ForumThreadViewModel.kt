package com.example.froumapp.ui.forum.categories.forums.threads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.ThreadForumRepository
import com.example.froumapp.data.repository.ThreadRepository
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumThreadViewModel @Inject constructor(
    private val repository: ThreadForumRepository
): BaseViewModel(repository) {
    private val _threadResponse: MutableLiveData<Resource<ThreadResponse>> = MutableLiveData()
    private val _createdThreadResponse: MutableLiveData<Resource<ThreadResponseItem>> = MutableLiveData()
    val threadResponse: LiveData<Resource<ThreadResponse>>
        get() = _threadResponse
    val createdThreadResponse: LiveData<Resource<ThreadResponseItem>>
        get() = _createdThreadResponse

    fun getThreadsByForumId(forumId: String) = viewModelScope.launch {
        _threadResponse.value = repository.getThreadsByForumId(forumId)
    }

    fun addThread(token: String, forumId: String, threadTitle: String, threadDescription: String, userId: String) = viewModelScope.launch{
        _createdThreadResponse.value = repository.addThread(token, forumId, threadTitle, threadDescription, userId)
    }
}