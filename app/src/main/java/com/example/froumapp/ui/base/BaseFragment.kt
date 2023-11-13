package com.example.froumapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.froumapp.R
import com.example.froumapp.data.UserPreferences
import com.example.froumapp.data.network.RemoteDataSource
import com.example.froumapp.ui.auth.AuthActivity
import com.example.froumapp.ui.forum.ForumActivity
import com.example.froumapp.ui.forum.home.HomeFragment
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)

        if (activity is ForumActivity) {
            bottomBar = (activity as ForumActivity).findViewById(R.id.bottomNavigationView)
            enableBottomBar()
        }

        lifecycleScope.launch {
            token = userPreferences.authToken.first()
            userId = userPreferences.userId.first()
        }
        return binding.root
    }

    fun enableBottomBar() {
        bottomBar!!.visibility = View.VISIBLE
    }

    fun disableBottomBar() {
        bottomBar!!.visibility = View.GONE
    }

    fun logout() = lifecycleScope.launch {
        userPreferences.removeUserCredentials()
        requireActivity().startNewActivity(AuthActivity::class.java)
    }


    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
}