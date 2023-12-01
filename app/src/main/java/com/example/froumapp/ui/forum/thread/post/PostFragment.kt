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
        disableBottomBar()
        setFragmentTitle("Add Post")
        setNavigationDestination(PostFragmentDirections.actionPostFragmentToThreadFragment(args.threadId, true, null, null, null, null))
        super.onViewCreated(view, savedInstanceState)
        binding.addButton.setOnClickListener {
            viewModel.addPost(userId!!, binding.postMessageInput.text.toString(), args.threadId)
        }
        viewModel.post.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val action =
                        PostFragmentDirections.actionPostFragmentToThreadFragment(args.threadId, true, null, null, null, null)
                    findNavController().navigate(action)
                }

                is Resource.Failure -> handleApiError(it)
            }
        }
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostBinding = FragmentPostBinding.inflate(inflater, container, false)
}