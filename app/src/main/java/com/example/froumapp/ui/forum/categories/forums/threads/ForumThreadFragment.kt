package com.example.froumapp.ui.forum.categories.forums.threads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.databinding.FragmentForumThreadBinding
import com.example.froumapp.ui.MarginItemDecoration
import com.example.froumapp.ui.adapters.ThreadListAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForumThreadFragment : BaseFragment<FragmentForumThreadBinding>() {
    private val viewModel: ForumThreadViewModel by viewModels()
    private lateinit var adapter: ThreadListAdapter
    private val args: ForumThreadFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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