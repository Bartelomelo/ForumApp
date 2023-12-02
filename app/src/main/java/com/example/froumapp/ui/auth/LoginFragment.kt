package com.example.froumapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.froumapp.data.network.Resource
import com.example.froumapp.databinding.FragmentLoginBinding
import com.example.froumapp.ui.base.BaseFragment
import com.example.froumapp.ui.forum.ForumActivity
import com.example.froumapp.ui.handleApiError
import com.example.froumapp.ui.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: AuthViewModel by viewModels()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.progressbar.visibility = View.GONE
        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveUserCredentials(it.value.token, it.value.user._id)
                    }
                    requireActivity().startNewActivity(ForumActivity::class.java)
                }
                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> {}
            }

        })

        binding.loginButton.setOnClickListener {
            val userName = binding.login.text.toString().trim()
            val password = binding.password.text.toString().trim()
            // @todo walidacja
            viewModel.login(userName, password)
        }
    }

//    override fun getViewModel(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

}