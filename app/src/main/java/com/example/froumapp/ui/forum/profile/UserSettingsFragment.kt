package com.example.froumapp.ui.forum.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.froumapp.R
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.User
import com.example.froumapp.databinding.FragmentUserSettingsBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSettingsFragment : BaseFragment<FragmentUserSettingsBinding>() {

    private val viewModel: ProfileViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableBottomBar()
        setFragmentTitle("Ustawienia")
        setNavigationDestination(
            UserSettingsFragmentDirections.actionUserSettingsFragment3ToProfileFragment(
                "empty",
                null
            )
        )
        viewModel.getUser(userId!!)
        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    updateUI(it.value)
                }

                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
            }
        })

        binding.updateButton.setOnClickListener {
            Log.d("token", token!!)
            viewModel.updateUser(
                "Bearer $token",
                userId!!,
                binding.nickInput.text.toString(),
                binding.emailInput.text.toString(),
                binding.aboutInput.text.toString(),
                binding.signatureInput.text.toString()
            )
            viewModel.updateResponse.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Success -> {
                        binding.progressbar.visibility = View.GONE
                        findNavController().navigate(R.id.action_userSettingsFragment3_to_profileFragment)
                    }

                    is Resource.Failure -> {
                        Log.d("request", it.toString())
                        handleApiError(it)
                    }
                    is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
                }
            })
        }


    }

    private fun updateUI(user: User) {
        binding.nickInput.setText(user.username)
        binding.emailInput.setText(user.email)
        binding.signatureInput.setText(user.signature)
        binding.aboutInput.setText(user.about)
        binding.userId.text = userId
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserSettingsBinding = FragmentUserSettingsBinding.inflate(inflater, container, false)

}