package com.example.froumapp.ui.forum.thread

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.R
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.databinding.FragmentThreadBinding
import com.example.froumapp.ui.MarginItemDecoration
import com.example.froumapp.ui.adapters.PostListAdapter
import com.example.froumapp.ui.adapters.ThreadListAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.forum.home.HomeFragmentDirections
import com.example.froumapp.ui.handleApiError
import com.example.froumapp.ui.snackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThreadFragment : BaseFragment<FragmentThreadBinding>() {

    private val viewModel: ThreadViewModel by viewModels()
    private val args: ThreadFragmentArgs by navArgs()
    private lateinit var adapter: PostListAdapter
    private lateinit var thread: ThreadResponseItem


    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableBottomBar()
        setFragmentTitle("Thread")
        if (args.isFromCategory) {
            setNavigationDestination(
                ThreadFragmentDirections.actionThreadFragmentToForumThreadFragment(
                    args.forumId!!,
                    args.forumName!!,
                    args.categoryId!!,
                    args.categoryName!!
                )
            )
        } else {
            setNavigationDestination(ThreadFragmentDirections.actionThreadFragmentToHomeFragment())
        }
        viewModel.getThreadById(args.thradId)
        viewModel.threadResponseItem.observe(this.viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    updateUI(it.value)
                    thread = it.value
                }

                is Resource.Failure -> handleApiError(it)
            }
        }
        binding.followButton.setOnClickListener {
            viewModel.followUnfollowThread("Bearer $token", thread._id, "1", userId!!)
            viewModel.followUnfollowMessage.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        binding.unfollowButton.visibility = View.VISIBLE
                        binding.followButton.visibility = View.GONE
                    }

                    is Resource.Failure -> handleApiError(it)
                }
            }
        }
        binding.unfollowButton.setOnClickListener {
            viewModel.followUnfollowThread("Bearer $token", thread._id, "0", userId!!)
            viewModel.followUnfollowMessage.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        binding.followButton.visibility = View.VISIBLE
                        binding.unfollowButton.visibility = View.GONE
                    }

                    is Resource.Failure -> handleApiError(it)
                }
            }
        }
        binding.threadProfilePicture.setOnClickListener {
            Log.d("authorId", thread.author._id)
            val action =
                ThreadFragmentDirections.actionThreadFragmentToProfileFragment(
                    thread.author._id,
                    thread._id,
                    args.isFromCategory,
                    args.forumId,
                    args.forumName,
                    args.categoryId,
                    args.categoryName
                )
            findNavController().navigate(action)
        }
        binding.replyButton.setOnClickListener {
            val action = ThreadFragmentDirections.actionThreadFragmentToPostFragment(thread._id)
            findNavController().navigate(action)
        }

    }

    private fun updateUI(thread: ThreadResponseItem) {
        binding.threadAuthorName.text = thread.author.username
        binding.threadTitle.text = thread.title
        binding.threadDescription.text = thread.description
        when (thread.author._id) {
            userId -> {
                binding.followButton.visibility = View.GONE
                binding.unfollowButton.visibility = View.GONE
            }

            else -> {
                if (thread.followers?.contains(userId!!) == true) {
                    binding.followButton.visibility = View.GONE
                    binding.unfollowButton.visibility = View.VISIBLE
                } else {
                    binding.followButton.visibility = View.VISIBLE
                    binding.unfollowButton.visibility = View.GONE
                }
            }
        }
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