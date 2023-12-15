package com.example.froumapp.ui.forum.profile

import android.R.attr.bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.froumapp.R
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.User
import com.example.froumapp.databinding.FragmentUserSettingsBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File


@AndroidEntryPoint
class UserSettingsFragment : BaseFragment<FragmentUserSettingsBinding>() {

    private val viewModel: ProfileViewModel by viewModels()
    private var imageUri: String? = null
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.userImage.setImageURI(uri)
        }


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

        binding.userImage.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.updateButton.setOnClickListener {
            val file = File(requireContext().cacheDir, "profile.jpg")
            file.createNewFile()
            file.outputStream().use {
                val bos = ByteArrayOutputStream()
                binding.userImage.drawToBitmap()
                    .compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
                ByteArrayInputStream(bos.toByteArray()).copyTo(it)
            }
            viewModel.uploadProfilePicture(
                binding.nickInput.text.toString(),
                "profile_photo.jpeg",
                file
            )
            viewModel.messageResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        binding.progressbar.visibility = View.GONE
                    }

                    is Resource.Failure -> {
                        Log.d("request", it.toString())
                        handleApiError(it)
                    }

                    is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
                }
            }
            Log.d("token", token!!)
            viewModel.updateUser(
                "Bearer $token",
                userId!!,
                binding.nickInput.text.toString(),
                binding.emailInput.text.toString(),
                binding.aboutInput.text.toString(),
                binding.signatureInput.text.toString(),
                userPicture = "profile_photo.jpeg"
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
        Picasso.with(requireContext())
            .load("http://10.0.2.2:5000/public/images/users/${user.username}/${user.profilePicture}")
            .error(ContextCompat.getDrawable(requireContext(), R.drawable.empty_profile_picture))
            .into(binding.userImage)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserSettingsBinding = FragmentUserSettingsBinding.inflate(inflater, container, false)

}