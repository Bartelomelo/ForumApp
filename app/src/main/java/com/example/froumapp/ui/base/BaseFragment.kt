package com.example.froumapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.froumapp.data.UserPreferences
import com.example.froumapp.data.network.RemoteDataSource
import com.example.froumapp.data.network.UserApi
import com.example.froumapp.data.repository.BaseRepository
import com.example.froumapp.ui.auth.AuthActivity
import com.example.froumapp.ui.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<B: ViewBinding, R: BaseRepository> : Fragment() {
    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
//    protected lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
//        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        lifecycleScope.launch {
            userPreferences.authToken.first()
            userPreferences.userId.first()
        }
        return binding.root
    }

    fun logout() = lifecycleScope.launch {
        val api = remoteDataSource.buildApi(UserApi::class.java, null, null)
//        viewModel.logout(api)
        userPreferences.removeUserCredentials()
        requireActivity().startNewActivity(AuthActivity::class.java)
    }

//    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R

}