package com.example.froumapp.ui.base

import androidx.lifecycle.ViewModel
import com.example.froumapp.data.repository.BaseRepository

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

}