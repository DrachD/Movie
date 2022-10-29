package com.dmitriy.movie.adapters

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dmitriy.movie.R
import com.dmitriy.movie.databinding.ItemPopularMovieBinding
import com.dmitriy.movie.model.MovieDetails
import com.dmitriy.movie.model.data.popularmovie.Result
import com.squareup.picasso.Picasso

const val KEY_MOVIE_DETAILS = "MOVIE_DETAILS"

class PopularMoviesAdapter(
    private val fragment: Fragment,
    private val popularMovieList: List<Result?>?
    ) : RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesViewHolder>() {

    private var binding: ItemPopularMovieBinding? = null

    inner class PopularMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var movieDetails: MovieDetails? = null

        init {
            imageView = itemView.findViewById(R.id.popularMovieImageView)
            imageView.setOnClickListener {
                onMovieDetailsButtonPressed()
            }
        }

        private fun onMovieDetailsButtonPressed() {
            Log.d("logs", movieDetails?.title!!)
            fragment.findNavController().navigate(
                R.id.detailsFragment,
                bundleOf(KEY_MOVIE_DETAILS to movieDetails)
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        binding = ItemPopularMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PopularMoviesViewHolder(binding?.root!!)
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        val movieItem = popularMovieList?.get(position)
        val imageId = "https://image.tmdb.org/t/p/w500/" + movieItem?.poster_path
        Glide.with(holder.itemView)
            .load(imageId)
            .into(holder.imageView)

        val movieDetails = MovieDetails(
            id = movieItem?.id!!,
            adult = movieItem.adult!!,
            genre = movieItem.genre_ids!!,
            overview = movieItem.overview!!,
            releaseDate = movieItem.release_date!!,
            title = movieItem.title!!,
            voteAverage = movieItem.vote_average!!,
            voteCount = movieItem.vote_count!!,
            imageId = imageId
        )
        holder.movieDetails = movieDetails

//        Picasso.get()
//            .load("https://image.tmdb.org/t/p/w500/" + movieItem?.poster_path)
//            .into(holder.imageView)
    }

    override fun getItemCount(): Int = popularMovieList?.size!!
}