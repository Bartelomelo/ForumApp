package com.example.froumapp.ui.forum.thread

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
import com.example.froumapp.ui.adapters.ThreadImageAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThreadFragment : BaseFragment<FragmentThreadBinding>(),
    DialogFragment.DialogFragmentListener {

    private val viewModel: ThreadViewModel by viewModels()
    private val args: ThreadFragmentArgs by navArgs()
    private lateinit var adapter: PostListAdapter
    private lateinit var imagesAdapter: ThreadImageAdapter
    private lateinit var thread: ThreadResponseItem
    private var followersList: MutableList<String> = mutableListOf()

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
                    followersList = it.value.followers?.toMutableList() ?: mutableListOf()
                    setupMenu()

                }

                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
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
        imagesAdapter = ThreadImageAdapter(thread._id) {}
        adapter = PostListAdapter(token!!, userId!!, viewModel){}
        binding.threadImages.adapter = imagesAdapter
        binding.threadPosts.adapter = adapter
        imagesAdapter.submitList(thread.images)
        adapter.submitList(thread.posts)
        binding.threadImages.layoutManager = LinearLayoutManager(this.requireContext())
        binding.threadImages.addItemDecoration(
            MarginItemDecoration(15)
        )
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
                    R.id.action_follow -> {
                        if (followersList.contains(userId)) {
                            viewModel.followUnfollowThread("Bearer $token", args.thradId, 0)
                            menuItem.setIcon(R.drawable.baseline_follow)
                            menuItem.icon?.setTint(Color.parseColor("#ffffff"))
                            followersList.clear()
                        } else {
                            viewModel.followUnfollowThread("Bearer $token", args.thradId, 1)
                            menuItem.setIcon(R.drawable.baseline_unfollow)
                            menuItem.icon?.setTint(Color.parseColor("#ffffff"))
                            followersList.add(userId!!)
                        }
                        viewModel.followUnfollowMessage.observe(viewLifecycleOwner) {
                            when (it) {
                                is Resource.Success -> {
                                    Toast.makeText(requireContext(), "Akcja zakończona sukcesem.", Toast.LENGTH_SHORT).show()
                                }
                                is Resource.Failure -> handleApiError(it)
                                is Resource.Loading -> {}
                            }
                        }
                    }
                    R.id.action_delete -> {
                        val dialog =
                            DialogFragment("Usuń Wątek", "Czy napewno chcesz usunąć wątek?")
                        dialog.setTargetFragment(this@ThreadFragment, 1)
                        dialog.show(requireFragmentManager(), "DELETE_THREAD")
                    }

                    R.id.action_edit -> {
                        val action =
                            ThreadFragmentDirections.actionThreadFragmentToEditThreadFragment(
                                thread._id,
                                args.forumName,
                                args.categoryName,
                                args.isFromCategory,
                                args.forumId,
                                args.categoryId
                            )
                        findNavController().navigate(action)
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
        viewModel.deleteThread("Bearer $token", thread._id)
        viewModel.deleteResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Pomyślnie usunięto wątek,",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (args.isFromCategory) {
                        val action =
                            ThreadFragmentDirections.actionThreadFragmentToForumThreadFragment(
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

                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> {}
            }
        }
        Toast.makeText(requireContext(), "Pomyślnie usunięto wątek", Toast.LENGTH_SHORT).show()


    }
}