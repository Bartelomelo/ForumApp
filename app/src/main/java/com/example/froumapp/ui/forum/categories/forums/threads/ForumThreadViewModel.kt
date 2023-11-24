package com.example.froumapp.ui.forum.categories.forums.threads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.ThreadForumRepository
import com.example.froumapp.data.repository.ThreadRepository
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumThreadViewModel @Inject constructor(
    private val repository: ThreadForumRepository
): BaseViewModel(repository) {
    private val _threadResponse: MutableLiveData<Resource<ThreadResponse>> = MutableLiveData()
    val threadResponse: LiveData<Resource<ThreadResponse>>
        get() = _threadResponse

    fun getThreadsByForumId(forumId: String) = viewModelScope.launch {
        _threadResponse.value = repository.getThreadsByForumId(forumId)
    }
}