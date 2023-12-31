package com.example.froumapp.ui.base

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
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.froumapp.R
import com.example.froumapp.data.UserPreferences
import com.example.froumapp.data.network.RemoteDataSource
import com.example.froumapp.ui.auth.AuthActivity
import com.example.froumapp.ui.forum.ForumActivity
import com.example.froumapp.ui.startNewActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


abstract class BaseFragment<B : ViewBinding> : Fragment() {
    @Inject
    protected lateinit var userPreferences: UserPreferences
    protected open lateinit var binding: B
    protected val remoteDataSource = RemoteDataSource()

    @get:JvmName("functionOfKotlin")
    protected var userId: String? = null
    protected var token: String? = null
    private var bottomBar: BottomNavigationView? = null
    private var toolBar: Toolbar? = null
    protected var menuHost: MenuHost? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)

        if (activity is ForumActivity) {
            bottomBar = (activity as ForumActivity).findViewById(R.id.bottomNavigationView)
            toolBar = (activity as ForumActivity).findViewById(R.id.toolbar)
            menuHost = requireActivity()
            enableBackButton()
            enableBottomBar()
        }


        lifecycleScope.launch {
            token = userPreferences.authToken.first()
            userId = userPreferences.userId.first()
        }
        return binding.root
    }

    private fun enableBottomBar() {
        bottomBar!!.visibility = View.VISIBLE
    }

    fun disableBottomBar() {
        bottomBar!!.visibility = View.GONE
    }

    fun enableBackButton() {
        toolBar?.setNavigationIcon(R.drawable.baseline_arrow_back_24)
    }

    fun disableBackButton() {
        toolBar?.navigationIcon = null
    }

    fun setFragmentTitle(title: String) {
        toolBar?.title = title
    }

    fun setNavigationDestination(action: NavDirections) {
        toolBar?.setNavigationOnClickListener {
            findNavController().navigate(action)
        }
    }
    fun logout() = lifecycleScope.launch {
        userPreferences.removeUserCredentials()
        requireActivity().startNewActivity(AuthActivity::class.java)
    }


    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
}