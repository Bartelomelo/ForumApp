package com.example.froumapp.ui.forum.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.User
import com.example.froumapp.databinding.FragmentProfileBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()
    private val args: ProfileFragmentArgs by navArgs()
    private lateinit var currentUser: String
    private lateinit var user: User
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disableBackButton()
        setFragmentTitle("Profile")

        if (args.userId == "empty") {
            currentUser = userId!!
        } else {
            currentUser = args.userId!!
            setGuestUI()
            enableBackButton()
            setNavigationDestination(
                ProfileFragmentDirections.actionProfileFragmentToThreadFragment(
                    args.threadId!!,
                    args.isFromCategory,
                    args.forumId,
                    args.forumName,
                    args.categoryId,
                    args.categoryName
                )
            )
        }
        Log.d("args", args.userId!!)

        viewModel.getUser(currentUser)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    updateUI(it.value)
                    user = it.value
                }

                is Resource.Failure -> handleApiError(it)
            }
        })
        binding.settingsButton.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToUserSettingsFragment3(userId!!)
            findNavController().navigate(action)
        }
        binding.followButton.setOnClickListener {
            binding.followButton.visibility = View.GONE
            binding.unfollowButton.visibility = View.VISIBLE
        }

        binding.unfollowButton.setOnClickListener {
            binding.followButton.visibility = View.VISIBLE
            binding.unfollowButton.visibility = View.GONE
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun setGuestUI() {
        binding.settingsButton.visibility = View.GONE
        binding.logoutButton.visibility = View.GONE
        disableBottomBar()
    }

    private fun updateUI(user: User) {
        binding.username.text = user.username
        binding.userSignature.text = user.signature
        binding.aboutUser.text = user.about
        when (currentUser) {
            userId -> {
                binding.followButton.visibility = View.GONE
                binding.unfollowButton.visibility = View.GONE
            }

            else -> {
                if (user.followers.contains(userId!!)) {
                    binding.followButton.visibility = View.GONE
                    binding.unfollowButton.visibility = View.VISIBLE
                } else {
                    binding.followButton.visibility = View.VISIBLE
                    binding.unfollowButton.visibility = View.GONE
                }
            }
        }
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)
}