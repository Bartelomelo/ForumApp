package com.example.froumapp.ui.forum.categories.forums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.ForumResponse
import com.example.froumapp.databinding.FragmentForumBinding
import com.example.froumapp.ui.MarginItemDecoration
import com.example.froumapp.ui.adapters.ForumListAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForumFragment : BaseFragment<FragmentForumBinding>() {
    private val viewModel: ForumViewModel by viewModels()
    private val args: ForumFragmentArgs by navArgs()
    private lateinit var adapter: ForumListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentTitle(args.categoryName)
        setNavigationDestination(ForumFragmentDirections.actionForumFragmentToCategoriesFragment())
        viewModel.getForums(args.categoryId)
        viewModel.forumResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    updateUI(it.value)
                }
                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
            }
        }
    }

    private fun updateUI(forumResponse: ForumResponse) {
        adapter = ForumListAdapter {
            val action = ForumFragmentDirections.actionForumFragmentToForumThreadFragment(it._id, it.name, args.categoryId, args.categoryName)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(forumResponse.forums)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(25)
        )
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentForumBinding.inflate(inflater, container, false)

}