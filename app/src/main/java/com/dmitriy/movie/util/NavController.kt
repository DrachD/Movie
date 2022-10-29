package com.dmitriy.movie.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.dmitriy.movie.R

fun Fragment.findNavController() = (
        this.requireActivity().supportFragmentManager
            .findFragmentById(R.id.fragmentContainer) as NavHostFragment
            ).navController