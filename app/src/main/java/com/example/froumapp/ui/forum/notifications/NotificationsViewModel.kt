package com.example.froumapp.ui.forum.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.repository.NotificationsRepository
import com.example.froumapp.data.responses.MessageResponse
import com.example.froumapp.data.responses.NotificationResponse
import com.example.froumapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: NotificationsRepository
) : BaseViewModel(repository) {
    private var _notificationResponse: MutableLiveData<Resource<NotificationResponse>> = MutableLiveData()
    private var _deleteNotificationResponse: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()
    val notificationResponse: LiveData<Resource<NotificationResponse>>
        get() = _notificationResponse
    val deleteNotificationResponse: LiveData<Resource<MessageResponse>>
        get() = _deleteNotificationResponse

    fun getNotifications(token: String) = viewModelScope.launch {
        _notificationResponse.value = repository.getNotifications(token)
    }

    fun deleteNotifications(token: String) = viewModelScope.launch {
        _deleteNotificationResponse.value = repository.deleteNotifications(token)
    }
}