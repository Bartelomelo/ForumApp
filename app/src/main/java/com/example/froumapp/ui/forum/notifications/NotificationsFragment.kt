package com.example.froumapp.ui.forum.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.froumapp.R
import com.example.froumapp.data.network.Resource
import com.example.froumapp.data.responses.NotificationResponse
import com.example.froumapp.databinding.FragmentNotificationsBinding
import com.example.froumapp.ui.MarginItemDecoration
import com.example.froumapp.ui.adapters.NotificationsListAdapter
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.forum.categories.forums.ForumFragmentArgs
import com.example.froumapp.ui.handleApiError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {
    private val viewModel: NotificationsViewModel by viewModels()
    private val args: ForumFragmentArgs by navArgs()
    private lateinit var adapter: NotificationsListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disableBackButton()
        toolbarInflateMenu()
        setToolbarIcon(R.drawable.baseline_delete_24)
        setFragmentTitle("Notifications")
        viewModel.getNotifications("Bearer $token")
        viewModel.notificationResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("notifications", it.value.toString())
                    binding.progressbar.visibility = View.GONE
                    updateUI(it.value)
                }

                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
            }
        }
        toolBar?.menu?.getItem(0)?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_follow -> {
                    viewModel.deleteNotifications("Bearer $token")
                    viewModel.deleteNotificationResponse.observe(viewLifecycleOwner) {
                        when (it) {
                            is Resource.Success -> {
                                viewModel.getNotifications("Bearer $token")
                                binding.progressbar.visibility = View.GONE
                            }
                            is Resource.Failure -> handleApiError(it)
                            is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
                        }
                    }
                    true
                }
                else -> {false}
            }
        }
    }

    private fun updateUI(notificationResponse: NotificationResponse) {
        adapter = NotificationsListAdapter {
            val action =
                NotificationsFragmentDirections.actionNotificationsFragmentToThreadFragment(
                    thradId = it.thread._id,
                    isFromNotification = true
                )
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(notificationResponse)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.addItemDecoration(MarginItemDecoration(10))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearItemMenu()
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationsBinding =
        FragmentNotificationsBinding.inflate(inflater, container, false)

}