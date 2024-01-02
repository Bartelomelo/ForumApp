package com.example.froumapp.ui.forum.thread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.ThreadResponseItem
import com.example.froumapp.databinding.FragmentEditThreadBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditThreadFragment : BaseFragment<FragmentEditThreadBinding>() {
    private val viewModel: ThreadViewModel by viewModels()
    private val args: EditThreadFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableBottomBar()
        setFragmentTitle("Thread Update")
        setNavigationDestination(
            EditThreadFragmentDirections.actionEditThreadFragmentToThreadFragment(
                args.threadId,
                args.isFromCategory,
                args.forumId,
                args.forumName,
                args.categoryId,
                args.categoryName
            )
        )
        viewModel.getThreadById(args.threadId)
        viewModel.threadResponseItem.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    updateUI(it.value)
                    binding.progressbar.visibility = View.GONE
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
            }
        }
        binding.updateButton.setOnClickListener {
            viewModel.updateThread("Bearer $token", args.threadId, binding.threadDescriptionInput.text.toString())
            viewModel.updateThreadResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Wątek został zaaktalizowany.", Toast.LENGTH_SHORT).show()
                        val action = EditThreadFragmentDirections.actionEditThreadFragmentToThreadFragment(
                            args.threadId,
                            args.isFromCategory,
                            args.forumId,
                            args.forumName,
                            args.categoryId,
                            args.categoryName
                        )
                        findNavController().navigate(action)
                        //TODO("dodać edytowanie zdjęć")
                    }
                    is Resource.Failure -> handleApiError(it)
                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun updateUI(thread: ThreadResponseItem) {
        binding.threadTitleInput.setText(thread.title)
        binding.threadDescriptionInput.setText(thread.description)
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditThreadBinding = FragmentEditThreadBinding.inflate(inflater, container, false)

}