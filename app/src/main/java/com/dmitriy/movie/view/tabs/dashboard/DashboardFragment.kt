package com.dmitriy.movie.view.tabs.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.dmitriy.movie.R
import com.dmitriy.movie.adapters.PopularMoviesAdapter
import com.dmitriy.movie.databinding.FragmentDashboardBinding
import com.dmitriy.movie.util.findNavController
import com.dmitriy.movie.viewmodel.MoviesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var popularMoviesAdapter: PopularMoviesAdapter

    private val moviesViewModel by lazy {
        ViewModelProvider(this)[MoviesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesViewModel.getPopularMovies()
        auth = Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        binding.popularMoviesRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        updateUI()
    }

    private fun updateUI() {
        moviesViewModel.popularMovies.observe(viewLifecycleOwner) {
            binding.popularMoviesRecyclerView.apply {
                popularMoviesAdapter = PopularMoviesAdapter(this@DashboardFragment, it)
                adapter = popularMoviesAdapter
            }
        }
    }

    private fun onSignOutButtonPressed() {
        auth.signOut()

//        val navHost = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
//        navHost.navController.navigate(R.id.introductionFragment, null, navOptions {
//            popUpTo(R.id.tabsFragment) {
//                inclusive = true
//            }
//        })
        findNavController().navigate(R.id.introductionFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })
    }
}