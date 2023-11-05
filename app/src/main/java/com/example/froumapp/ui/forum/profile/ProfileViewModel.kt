package com.example.froumapp.ui.forum.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.UserPreferences
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.BaseRepository
import com.example.froumapp.data.repository.ProfileRepository
import com.example.froumapp.data.responses.LoginResponse
import com.example.froumapp.data.responses.User
import com.example.froumapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
): BaseViewModel(repository) {

    private val _user: MutableLiveData<Resource<User>> = MutableLiveData()
    val user: LiveData<Resource<User>>
        get() = _user
    fun getUser(userId: String) = viewModelScope.launch {
        _user.value = repository.getUser(userId)
    }
}