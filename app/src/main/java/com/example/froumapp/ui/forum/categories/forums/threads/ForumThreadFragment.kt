package com.example.froumapp.ui.forum.categories.forums.threads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.R
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.databinding.FragmentForumThreadBinding
import com.example.froumapp.ui.MarginItemDecoration
import com.example.froumapp.ui.adapters.ThreadListAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.forum.ForumActivity
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForumThreadFragment : BaseFragment<FragmentForumThreadBinding>() {
    private val viewModel: ForumThreadViewModel by viewModels()
    private lateinit var adapter: ThreadListAdapter
    private val args: ForumThreadFragmentArgs by navArgs()
    private var toolBar: Toolbar? = null
    private var followersList: MutableList<String> = mutableListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersList = args.followers?.toMutableList() ?: mutableListOf()
        toolBar = (activity as ForumActivity).findViewById(R.id.toolbar)
        setFragmentTitle(args.forumName)
        toolbarInflateMenu()
        setNavigationDestination(
            ForumThreadFragmentDirections.actionForumThreadFragmentToForumFragment(
                args.categoryId,
                args.categoryName
            )
        )
        if (args.followers?.toList()?.contains(userId!!) == true) {
            setToolbarUnfollowIcon()
        } else {
            setToolbarFollowIcon()
        }
        toolBar?.menu?.getItem(0)?.setOnMenuItemClickListener { it ->
            when (it.itemId) {
                R.id.action_follow -> {
                    when (followersList.contains(userId!!)) {
                        true -> {
                            viewModel.followForum("Bearer $token", args.forumId, 0)
                            viewModel.followResponse.observe(viewLifecycleOwner) {
                                when (it) {
                                    is Resource.Success -> {
                                        binding.progressbar.visibility = View.GONE
                                        followersList.remove(userId!!)
                                        setToolbarFollowIcon()
                                    }

                                    is Resource.Failure -> handleApiError(it)
                                    is Resource.Loading -> binding.progressbar.visibility =
                                        View.VISIBLE
                                }
                            }
                        }

                        false -> {
                            viewModel.followForum("Bearer $token", args.forumId, 1)
                            viewModel.followResponse.observe(viewLifecycleOwner) {
                                when (it) {
                                    is Resource.Success -> {
                                        binding.progressbar.visibility = View.GONE
                                        followersList.add(userId!!)
                                        setToolbarUnfollowIcon()
                                    }

                                    is Resource.Failure -> handleApiError(it)
                                    is Resource.Loading -> binding.progressbar.visibility =
                                        View.VISIBLE
                                }
                            }
                        }
                    }
                    true
                }

                else -> {
                    false
                }
            }
        }
        viewModel.getThreadsByForumId(args.forumId)
        viewModel.threadResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    updateUI(it.value)
                    Log.d("Success for ForumThreadFragment", it.value.toString())
                    binding.progressbar.visibility = View.GONE
                }

                is Resource.Failure -> {
                    handleApiError(it)
                }

                is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
            }
        }
        binding.addButton.setOnClickListener {
            val action = ForumThreadFragmentDirections.actionForumThreadFragmentToAddThreadFragment(
                args.forumId,
                args.forumName,
                args.categoryId,
                args.categoryName,
                null
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearItemMenu()
    }

    private fun updateUI(threadResponse: ThreadResponse) {
        adapter = ThreadListAdapter {
            val action = ForumThreadFragmentDirections.actionForumThreadFragmentToThreadFragment(
                it._id,
                true,
                args.forumId,
                args.forumName,
                args.categoryId,
                args.categoryName
            )
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(threadResponse)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(25)
        )
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentForumThreadBinding.inflate(inflater, container, false)

}