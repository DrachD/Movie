package com.dmitriy.movie.view.tabs.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dmitriy.movie.R
import com.dmitriy.movie.adapters.KEY_MOVIE_DETAILS
import com.dmitriy.movie.databinding.FragmentDetailsBinding
import com.dmitriy.movie.model.MovieDetails

const val KEY_MOVIE_DETAILS_ID = "MOVIE_DETAILS_ID"

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private var movieDetails: MovieDetails? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        binding.fullDetailsButton.setOnClickListener { onFullDetailsButtonPressed() }

        movieDetails = requireArguments().getParcelable<MovieDetails>(KEY_MOVIE_DETAILS)

        binding.apply {
            Glide.with(view)
                .load(movieDetails?.imageId)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(movieImage)
            detailsMovieTitle.text = movieDetails?.title
            detailsMovieOverview.text = movieDetails?.overview
        }
    }

    private fun onFullDetailsButtonPressed() {
        findNavController().navigate(R.id.action_detailsFragment_to_webViewFragment, bundleOf(
            KEY_MOVIE_DETAILS_ID to movieDetails?.id
        ))
    }
}