package com.example.froumapp.ui.forum.thread

import android.annotation.SuppressLint
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.R
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.databinding.FragmentThreadBinding
import com.example.froumapp.ui.DialogFragment
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
class ThreadFragment : BaseFragment<FragmentThreadBinding>(),
    DialogFragment.DialogFragmentListener {

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
                    setupMenu()

                }

                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
            }
        }

        binding.followButton.setOnClickListener {
            viewModel.followUnfollowThread("Bearer $token", thread._id, 1)
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
            viewModel.followUnfollowThread("Bearer $token", thread._id, 0)
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

    private fun setupMenu() {
        menuHost?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.action_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        val dialog =
                            DialogFragment("Usuń Wątek", "Czy napewno chcesz usunąć wątek?")
                        dialog.setTargetFragment(this@ThreadFragment, 1)
                        dialog.show(requireFragmentManager(), "DELETE_THREAD")
                    }
                    R.id.action_edit -> {
                        Toast.makeText(requireContext(), "Edytuję post!", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                when (thread.author._id == userId!!) {
                    true -> {
                        menu.findItem(R.id.action_follow).isVisible = false
                        menu.findItem(R.id.action_edit).icon?.setTint(Color.parseColor("#ffffff"))
                        menu.findItem(R.id.action_delete).icon?.setTint(Color.parseColor("#ffffff"))
                    }

                    false -> {
                        menu.findItem(R.id.action_delete).isVisible = false
                        menu.findItem(R.id.action_edit).isVisible = false
                        menu.findItem(R.id.action_follow).icon?.setTint(Color.parseColor("#ffffff"))
                    }
                }


            }
        }, viewLifecycleOwner)
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentThreadBinding = FragmentThreadBinding.inflate(inflater, container, false)

    override fun onDialogPositiveClick(dialog: androidx.fragment.app.DialogFragment) {
        //TODO("Add thread delete")
        Toast.makeText(requireContext(), "Pomyślnie usunięto wątek", Toast.LENGTH_SHORT).show()

        if (args.isFromCategory) {
            val action = ThreadFragmentDirections.actionThreadFragmentToForumThreadFragment(
                args.forumId!!,
                args.forumName!!,
                args.categoryId!!,
                args.categoryName!!
            )
            findNavController().navigate(action)
        } else {
            val action = ThreadFragmentDirections.actionThreadFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}