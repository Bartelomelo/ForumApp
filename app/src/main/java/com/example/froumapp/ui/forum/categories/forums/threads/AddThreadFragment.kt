package com.example.froumapp.ui.forum.categories.forums.threads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.froumapp.data.network.Resource
import com.example.froumapp.databinding.FragmentAddThreadBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddThreadFragment : BaseFragment<FragmentAddThreadBinding>() {
    private val viewModel: ForumThreadViewModel by viewModels()
    private val args: ForumThreadFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableBottomBar()
        binding.progressbar.visibility = View.GONE
        setFragmentTitle("Add Thread")
        setNavigationDestination(
            AddThreadFragmentDirections.actionAddThreadFragmentToForumThreadFragment(
                args.forumId,
                args.forumName,
                args.categoryId,
                args.categoryName
            )
        )

        binding.addButton.setOnClickListener {
            viewModel.addThread(
                "Bearer $token",
                args.forumId,
                binding.threadTitleInput.text.toString(),
                binding.threadDescriptionInput.text.toString(),
                userId!!
            )
        }
        viewModel.createdThreadResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val action =
                        AddThreadFragmentDirections.actionAddThreadFragmentToThreadFragment(
                            it.value._id,
                            true,
                            args.forumId,
                            args.forumName,
                            args.categoryId,
                            args.categoryName
                        )
                    findNavController().navigate(action)
                }

                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> {}
            }
        }

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddThreadBinding = FragmentAddThreadBinding.inflate(inflater, container, false)
}