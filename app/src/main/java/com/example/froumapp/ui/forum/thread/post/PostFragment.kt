package com.example.froumapp.ui.forum.thread.post

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
import com.example.froumapp.databinding.FragmentPostBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttp

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>() {
    private val viewModel: PostViewModel by viewModels()
    private val args: PostFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableBottomBar()
        setFragmentTitle("Add Post")
        setNavigationDestination(
            PostFragmentDirections.actionPostFragmentToThreadFragment(
                args.threadId,
                args.isFromCategory,
                args.forumId,
                args.forumName,
                args.categoryId,
                args.categoryName
            )
        )
        binding.addButton.setOnClickListener {
            viewModel.addPost(
                "Bearer $token",
                userId!!,
                binding.postMessageInput.text.toString(),
                args.threadId
            )
        }
        viewModel.post.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val action =
                        PostFragmentDirections.actionPostFragmentToThreadFragment(
                            args.threadId,
                            args.isFromCategory,
                            args.forumId,
                            args.forumName,
                            args.categoryId,
                            args.categoryName
                        )
                    findNavController().navigate(action)
                }

                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> {}
            }
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostBinding = FragmentPostBinding.inflate(inflater, container, false)
}