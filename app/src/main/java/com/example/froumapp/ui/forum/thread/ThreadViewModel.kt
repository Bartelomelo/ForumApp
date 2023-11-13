package com.example.froumapp.ui.forum.thread

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.ThreadRepository
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.data.responses.UpdateResponse
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val repository: ThreadRepository
): BaseViewModel(repository) {
    private val _threadResponseItem: MutableLiveData<Resource<ThreadResponseItem>> = MutableLiveData()
    val threadResponseItem: LiveData<Resource<ThreadResponseItem>>
        get() = _threadResponseItem

    fun getThreadById(threadId: String) = viewModelScope.launch {
        _threadResponseItem.value = repository.getThreadById(threadId)
    }
}