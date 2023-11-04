package com.example.froumapp.ui.base

import androidx.lifecycle.ViewModel
import com.example.froumapp.data.network.UserApi
import com.example.froumapp.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

    suspend fun logout(api: UserApi) = repository.logout(api)

}