package com.dmitriy.movie.repository.popularmovies

import com.dmitriy.movie.model.api.RetrofitInstance
import com.dmitriy.movie.model.data.popularmovie.PopularMovieList
import com.dmitriy.movie.util.Constants.API_KEY
import retrofit2.Call

class PopularMoviesRepositoryImpl : PopularMoviesRepository {

    private val apiService = RetrofitInstance.apiService

    override fun getPopularMovies(): Call<PopularMovieList> {
        return apiService.getPopularMovies(API_KEY, "en-US", 1)
    }
}