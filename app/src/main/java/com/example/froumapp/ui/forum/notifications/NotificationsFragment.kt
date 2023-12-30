package com.example.froumapp.ui.forum.notifications

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
        //setToolbarIcon(R.drawable.baseline_delete_24)
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
        setupMenu()
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
                        viewModel.deleteNotifications("Bearer $token")
                        viewModel.deleteNotificationResponse.observe(viewLifecycleOwner) {
                            when (it) {
                                is Resource.Success -> {
                                    viewModel.getNotifications("Bearer $token")
                                    binding.progressbar.visibility = View.GONE
                                    Toast.makeText(requireContext(), "Pomyślnie usunięto powiadomienia.", Toast.LENGTH_SHORT).show()
                                }

                                is Resource.Failure -> handleApiError(it)
                                is Resource.Loading -> binding.progressbar.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                return true
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                menu.findItem(R.id.action_edit).isVisible = false
                menu.findItem(R.id.action_follow).isVisible = false
                menu.findItem(R.id.action_delete).icon?.setTint(Color.parseColor("#ffffff"))
            }
        }, viewLifecycleOwner)
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

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationsBinding =
        FragmentNotificationsBinding.inflate(inflater, container, false)

}