package com.dmitriy.movie.fragments.loginRegister

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dmitriy.movie.R
import com.dmitriy.movie.databinding.FragmentIntruductionBinding
import com.dmitriy.movie.view.activities.MainActivity

class IntroductionFragment : Fragment(R.layout.fragment_intruduction) {

    private lateinit var binding: FragmentIntruductionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntruductionBinding.bind(view)
        binding.startAccountButton.setOnClickListener { onStartAccountPressed() }
    }

    private fun onStartAccountPressed() {
        val direction = IntroductionFragmentDirections.actionIntroductionFragmentToAccountOptionsFragment()
        findNavController().navigate(direction)
    }
}