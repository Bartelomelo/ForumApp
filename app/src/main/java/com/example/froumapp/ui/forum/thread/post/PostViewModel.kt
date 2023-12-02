package com.example.froumapp.ui.forum.thread.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.PostRepository
import com.example.froumapp.data.responses.Post
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
): BaseViewModel(repository) {

    private val _post: MutableLiveData<Resource<Post>> = MutableLiveData()
    val post: LiveData<Resource<Post>>
        get() = _post

    fun addPost(token: String, userId: String, comment: String, threadId: String) = viewModelScope.launch {
        _post.value = repository.addPost(token, userId, comment, threadId)
    }
}