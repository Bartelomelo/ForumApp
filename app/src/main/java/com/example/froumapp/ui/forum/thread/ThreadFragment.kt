package com.example.froumapp.ui.forum.thread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.databinding.FragmentThreadBinding
import com.example.froumapp.ui.MarginItemDecoration
import com.example.froumapp.ui.adapters.PostListAdapter
import com.example.froumapp.ui.adapters.ThreadListAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.forum.home.HomeFragmentDirections
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThreadFragment() : BaseFragment<FragmentThreadBinding>() {

    private val viewModel: ThreadViewModel by viewModels()
    private val args: ThreadFragmentArgs by navArgs()
    private lateinit var adapter: PostListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableBottomBar()
        viewModel.getThreadById(args.thradId)
        viewModel.threadResponseItem.observe(this.viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    updateUI(it.value)
                }
                is Resource.Failure -> handleApiError(it)
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(ThreadFragmentDirections.actionThreadFragmentToHomeFragment())
            enableBottomBar()
        }

    }

    private fun updateUI(thread: ThreadResponseItem) {
        binding.threadAuthorName.text = thread.author.username
        binding.threadTitle.text = thread.title
        binding.threadDescription.text = thread.description
        adapter = PostListAdapter {}
        binding.threadPosts.adapter = adapter
        adapter.submitList(thread.posts)
        binding.threadPosts.layoutManager = LinearLayoutManager(this.requireContext())
        binding.threadPosts.addItemDecoration(
            MarginItemDecoration(25)
        )
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentThreadBinding = FragmentThreadBinding.inflate(inflater, container, false)
}