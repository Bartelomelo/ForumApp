package com.example.froumapp.ui.forum.categories.forums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.ForumRepository
import com.example.froumapp.data.responses.ForumResponse
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val repository: ForumRepository
): BaseViewModel(repository) {
    private val _forumResponse: MutableLiveData<Resource<ForumResponse>> = MutableLiveData()
    val forumResponse: LiveData<Resource<ForumResponse>>
        get() = _forumResponse

    fun getForums(id: String) = viewModelScope.launch {
        _forumResponse.value = repository.getForums(id)
    }
}