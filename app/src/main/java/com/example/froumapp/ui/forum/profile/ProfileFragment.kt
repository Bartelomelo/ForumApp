package com.example.froumapp.ui.forum.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.froumapp.R
import com.example.froumapp.data.network.UserApi
import com.example.froumapp.data.repository.ProfileRepository
import com.example.froumapp.databinding.FragmentProfileBinding
import com.example.froumapp.ui.base.BaseFragment
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding, ProfileRepository>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    override fun getViewModel() = ProfileViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): ProfileRepository {
        val token = runBlocking { userPreferences.authToken.first() }
        val api = remoteDataSource.buildApi(UserApi::class.java, token)
        return ProfileRepository(api)
    }
}