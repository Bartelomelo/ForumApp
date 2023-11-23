package com.example.froumapp.ui.forum.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.CategoryResponse
import com.example.froumapp.data.responses.ThreadResponse
import com.example.froumapp.databinding.FragmentCategoriesBinding
import com.example.froumapp.ui.MarginItemDecoration
import com.example.froumapp.ui.adapters.CategoryListAdapter
import com.example.froumapp.ui.adapters.ThreadListAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.forum.home.HomeFragmentDirections
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding>() {
    private val viewModel: CategoriesViewModel by viewModels()
    private lateinit var adapter: CategoryListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCategories()
        viewModel.categoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    updateUI(it.value)
                }
                is Resource.Failure -> handleApiError(it)
            }
        }
    }

    private fun updateUI(categoryResponse: CategoryResponse) {
        adapter = CategoryListAdapter {

        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(categoryResponse)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.addItemDecoration(
            MarginItemDecoration(25)
        )
    }
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCategoriesBinding.inflate(inflater, container, false)

}