package com.example.froumapp.ui.forum.thread

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

@AndroidEntryPoint
class ThreadFragment : BaseFragment<FragmentThreadBinding>() {

    private val viewModel: ThreadViewModel by viewModels()
    private val args: ThreadFragmentArgs by navArgs()
    private lateinit var adapter: PostListAdapter
    private lateinit var thread: ThreadResponseItem


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
        } else if (args.isFromNotification) {
            setNavigationDestination(ThreadFragmentDirections.actionThreadFragmentToNotificationsFragment())
        } else {
            setNavigationDestination(ThreadFragmentDirections.actionThreadFragmentToHomeFragment())
        }
        viewModel.getThreadById(args.thradId)
        viewModel.threadResponseItem.observe(this.viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    updateUI(it.value)
                    thread = it.value
                }

                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
            }
        }
        binding.followButton.setOnClickListener {
            viewModel.followUnfollowThread("Bearer $token", thread._id, "1", userId!!)
            viewModel.followUnfollowMessage.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        binding.progressbar.visibility = View.GONE
                        binding.unfollowButton.visibility = View.VISIBLE
                        binding.followButton.visibility = View.GONE
                    }

                    is Resource.Failure -> handleApiError(it)
                    is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
                }
            }
        }
        binding.unfollowButton.setOnClickListener {
            viewModel.followUnfollowThread("Bearer $token", thread._id, "0", userId!!)
            viewModel.followUnfollowMessage.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        binding.progressbar.visibility = View.GONE
                        binding.followButton.visibility = View.VISIBLE
                        binding.unfollowButton.visibility = View.GONE
                    }

                    is Resource.Failure -> handleApiError(it)
                    is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
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
            val action = ThreadFragmentDirections.actionThreadFragmentToPostFragment(
                thread._id,
                args.isFromCategory,
                args.forumId,
                args.forumName,
                args.categoryId,
                args.categoryName
            )
            findNavController().navigate(action)
        }

    }

    private fun updateUI(thread: ThreadResponseItem) {
        binding.threadAuthorName.text = thread.author.username
        binding.threadTitle.text = thread.title
        binding.threadDescription.text = thread.description
        binding.views.text = thread.views.toString()
        binding.addDate.text = thread.createdAt.split("T")[0]
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
        Picasso.with(requireContext())
            .load("http://10.0.2.2:5000/public/images/users/${thread.author.username}/${thread.author.profilePicture}")
            .error(ContextCompat.getDrawable(requireContext(), R.drawable.empty_profile_picture))
            .into(binding.threadProfilePicture)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentThreadBinding = FragmentThreadBinding.inflate(inflater, container, false)
}