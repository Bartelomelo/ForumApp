package com.example.froumapp.ui.forum.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.froumapp.R
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.network.UserApi
import com.example.froumapp.data.repository.ProfileRepository
import com.example.froumapp.data.responses.User
import com.example.froumapp.databinding.FragmentProfileBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import com.example.froumapp.ui.snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding, ProfileRepository>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = runBlocking {
            userPreferences.userId.first()
        }
        viewModel.getUser(userId!!)
        viewModel.user.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    updateUI(it.value)
                }
                is Resource.Failure -> handleApiError(it)
            }
        })

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun updateUI(user: User) {
        binding.username.text = user.username
        binding.userSignature.text = user.signature
    }

    override fun getViewModel() = ProfileViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ProfileRepository {
        val token = runBlocking {
            userPreferences.authToken.first() }
        val userId = runBlocking {
            userPreferences.userId.first()
        }
        val api = remoteDataSource.buildApi(UserApi::class.java, token, userId)
        return ProfileRepository(api, userPreferences)
    }
}