package com.example.froumapp.ui.forum.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.ProfileRepository
import com.example.froumapp.data.responses.UpdateResponse
import com.example.froumapp.data.responses.UpdateUser
import com.example.froumapp.data.responses.User
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
): BaseViewModel(repository) {

    private val _updateResponse: MutableLiveData<Resource<UpdateResponse>> = MutableLiveData()
    val updateResponse: LiveData<Resource<UpdateResponse>>
        get() = _updateResponse

    private val _user: MutableLiveData<Resource<User>> = MutableLiveData()
    val user: LiveData<Resource<User>>
        get() = _user

    fun getUser(userId: String) = viewModelScope.launch {
        _user.value = repository.getUser(userId)
    }

    fun updateUser(
        token: String,
        userId: String,
        userName: String,
        email: String,
        about: String,
        signature: String,
    ) = viewModelScope.launch {
        val user = UpdateUser(about, email, signature, userName, userId)
        _updateResponse.value = repository.updateUser(token, userId, user)
    }
}