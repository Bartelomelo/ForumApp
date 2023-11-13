package com.example.froumapp.ui.forum.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.User
import com.example.froumapp.databinding.FragmentProfileBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser(userId!!)
        viewModel.user.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    updateUI(it.value)
                }
                is Resource.Failure -> handleApiError(it)
            }
        })
        binding.settingsButton.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToUserSettingsFragment3(userId!!)
            findNavController().navigate(action)
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun updateUI(user: User) {
        binding.username.text = user.username
        binding.userSignature.text = user.signature
        binding.aboutUser.setText(user.about)
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)
}