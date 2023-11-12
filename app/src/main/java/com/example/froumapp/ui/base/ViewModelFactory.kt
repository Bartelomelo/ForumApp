package com.example.froumapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.froumapp.data.repository.AuthRepository
import com.example.froumapp.data.repository.BaseRepository
import com.example.froumapp.data.repository.HomeRepository
import com.example.froumapp.data.repository.ProfileRepository
import com.example.froumapp.ui.auth.AuthViewModel
import com.example.froumapp.ui.forum.home.HomeViewModel
import com.example.froumapp.ui.forum.profile.ProfileViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository as ProfileRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as HomeRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass not found.")
        }
    }

}