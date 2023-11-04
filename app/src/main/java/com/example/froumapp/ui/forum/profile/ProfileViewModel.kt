package com.example.froumapp.ui.forum.profile

import com.example.froumapp.data.repository.BaseRepository
import com.example.froumapp.data.repository.ProfileRepository
import com.example.froumapp.ui.base.BaseViewModel

class ProfileViewModel(
    private val repository: ProfileRepository
): BaseViewModel(repository) {

}