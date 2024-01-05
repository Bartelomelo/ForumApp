package com.example.froumapp.ui.forum.categories.forums.threads

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.R
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.databinding.FragmentForumThreadBinding
import com.example.froumapp.ui.MarginItemDecoration
import com.example.froumapp.ui.adapters.ThreadImageAdapter
import com.example.froumapp.ui.adapters.ThreadListAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForumThreadFragment : BaseFragment<FragmentForumThreadBinding>() {
    private val viewModel: ForumThreadViewModel by viewModels()
    private lateinit var adapter: ThreadListAdapter
    private val args: ForumThreadFragmentArgs by navArgs()
    private var followersList: MutableList<String> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersList = args.followers?.toMutableList() ?: mutableListOf()
        setFragmentTitle(args.forumName)
        setNavigationDestination(
            ForumThreadFragmentDirections.actionForumThreadFragmentToForumFragment(
                args.categoryId,
                args.categoryName
            )
        )
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
        setupMenu()
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

    private fun setupMenu() {
        menuHost?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.action_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                when (menuItem.itemId) {
                    R.id.action_follow -> {
                        if (followersList.contains(userId)) {
                            viewModel.followForum("Bearer $token", args.forumId, 0)
                            menuItem.setIcon(R.drawable.baseline_follow)
                            menuItem.icon?.setTint(Color.parseColor("#ffffff"))
                            followersList.clear()
                        } else {
                            viewModel.followForum("Bearer $token", args.forumId, 1)
                            menuItem.setIcon(R.drawable.baseline_unfollow)
                            menuItem.icon?.setTint(Color.parseColor("#ffffff"))
                            followersList.add(userId!!)
                        }
                        viewModel.followResponse.observe(viewLifecycleOwner) {
                            when (it) {
                                is Resource.Success -> {
                                    Toast.makeText(requireContext(), "Akcja zakończona sukcesem.", Toast.LENGTH_SHORT).show()
                                }
                                is Resource.Failure -> handleApiError(it)
                                is Resource.Loading -> {}
                            }
                        }
                    }
                }
                return true
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                when (followersList.contains(userId)) {
                    true -> {
                        menu.findItem(R.id.action_follow).setIcon(R.drawable.baseline_unfollow)
                    }
                    false -> {
                        menu.findItem(R.id.action_follow).setIcon(R.drawable.baseline_follow)
                    }
                }
                menu.findItem(R.id.action_follow).icon?.setTint(Color.parseColor("#ffffff"))
                menu.findItem(R.id.action_edit).isVisible = false
                menu.findItem(R.id.action_delete).isVisible = false
            }
        }, viewLifecycleOwner)
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