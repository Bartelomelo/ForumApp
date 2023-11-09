package com.example.froumapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.AuthRepository
import com.example.froumapp.data.responses.LoginResponse
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): BaseViewModel(repository) {

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() =  _loginResponse

    fun login(
        userName: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = repository.login(userName, password)
    }

    suspend fun saveUserCredentials(token: String, userId: String) {
        repository.saveUserCredentials(token, userId)
    }


}