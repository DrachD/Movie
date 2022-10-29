package com.dmitriy.movie.fragments.loginRegister

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dmitriy.movie.R
import com.dmitriy.movie.databinding.FragmentAccountOptionsBinding

class AccountOptionsFragment : Fragment(R.layout.fragment_account_options) {

    private lateinit var binding: FragmentAccountOptionsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountOptionsBinding.bind(view)
        binding.buttonRegisterAccountOptions.setOnClickListener { onRegisterButtonPressed() }
        binding.buttonLoginAccountOptions.setOnClickListener { onLoginButtonPressed() }
    }

    private fun onLoginButtonPressed() {
        val direction = AccountOptionsFragmentDirections.actionAccountOptionsFragmentToLoginFragment()
        findNavController().navigate(direction)
    }

    private fun onRegisterButtonPressed() {
        val direction = AccountOptionsFragmentDirections.actionAccountOptionsFragmentToRegisterFragment()
        findNavController().navigate(direction)
    }
}