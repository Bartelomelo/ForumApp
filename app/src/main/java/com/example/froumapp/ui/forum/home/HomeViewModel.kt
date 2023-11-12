package com.example.froumapp.ui.forum.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.HomeRepository
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
): BaseViewModel(repository) {
    private val _threadResponse: MutableLiveData<Resource<ThreadResponse>> = MutableLiveData()
    val threadResponse: LiveData<Resource<ThreadResponse>>
        get() = _threadResponse

    fun getLastThreads() = viewModelScope.launch {
        _threadResponse.value = repository.getLastThreads()
    }
}