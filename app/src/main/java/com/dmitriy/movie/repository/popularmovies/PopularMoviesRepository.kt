package com.dmitriy.movie.repository.popularmovies

import com.dmitriy.movie.model.data.popularmovie.PopularMovieList
import retrofit2.Call

interface PopularMoviesRepository {

    fun getPopularMovies(): Call<PopularMovieList>
}