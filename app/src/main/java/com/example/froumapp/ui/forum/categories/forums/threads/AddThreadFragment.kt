package com.example.froumapp.ui.forum.categories.forums.threads

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.froumapp.data.network.Resource
import com.example.froumapp.databinding.FragmentAddThreadBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class AddThreadFragment : BaseFragment<FragmentAddThreadBinding>() {
    private val viewModel: ForumThreadViewModel by viewModels()
    private val args: ForumThreadFragmentArgs by navArgs()
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            binding.imageHolder.setImageURI(uri)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableBottomBar()
        binding.progressbar.visibility = View.GONE
        setFragmentTitle("Add Thread")
        setNavigationDestination(
            AddThreadFragmentDirections.actionAddThreadFragmentToForumThreadFragment(
                args.forumId,
                args.forumName,
                args.categoryId,
                args.categoryName
            )
        )

        binding.addButton.setOnClickListener {
            viewModel.addThread(
                "Bearer $token",
                args.forumId,
                binding.threadTitleInput.text.toString(),
                binding.threadDescriptionInput.text.toString(),
                userId!!
            )
        }
        binding.addPhoto.setOnClickListener {
            getContent.launch("image/*")
        }
        viewModel.createdThreadResponse.observe(viewLifecycleOwner) { createResponse ->
            when (createResponse) {
                is Resource.Success -> {
                    val action =
                        AddThreadFragmentDirections.actionAddThreadFragmentToThreadFragment(
                            createResponse.value._id,
                            true,
                            args.forumId,
                            args.forumName,
                            args.categoryId,
                            args.categoryName
                        )
                    val file = File(requireContext().cacheDir, "profile.jpg")
                    file.createNewFile()
                    file.outputStream().use {
                        val bos = ByteArrayOutputStream()
                        binding.imageHolder.drawToBitmap()
                            .compress(Bitmap.CompressFormat.PNG, 0, bos)
                        ByteArrayInputStream(bos.toByteArray()).copyTo(it)
                    }
                    viewModel.uploadThreadPicture(
                        createResponse.value._id,
                        "thread_photo.jpeg",
                        file
                    )
                    viewModel.messageResponse.observe(viewLifecycleOwner) {
                        when (it) {
                            is Resource.Success -> {
                                findNavController().navigate(action)
                                binding.progressbar.visibility = View.GONE
                            }

                            is Resource.Failure -> {
                                Log.d("request", it.toString())
                                handleApiError(it)
                            }

                            is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
                        }
                    }
                }

                is Resource.Failure -> handleApiError(createResponse)
                is Resource.Loading -> {}
            }
        }

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddThreadBinding = FragmentAddThreadBinding.inflate(inflater, container, false)
}