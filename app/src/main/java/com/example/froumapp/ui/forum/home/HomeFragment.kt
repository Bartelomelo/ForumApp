package com.example.froumapp.ui.forum.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.R
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.databinding.FragmentHomeBinding
import com.example.froumapp.ui.MarginItemDecoration
import com.example.froumapp.ui.adapters.ThreadListAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: ThreadListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLastThreads()
        viewModel.threadResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    updateUI(it.value)
                    Log.d("Success for Home", it.value.toString())
                }

                is Resource.Failure -> handleApiError(it)
            }
            val action = HomeFragmentDirections.actionHomeFragmentToThreadFragment("655918520bb30174ce44a198")
            setNavigationDestination(action)
        }
    }

    private fun updateUI(threadResponse: ThreadResponse) {
        adapter = ThreadListAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToThreadFragment(it._id)
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
    ) = FragmentHomeBinding.inflate(inflater, container, false)

}