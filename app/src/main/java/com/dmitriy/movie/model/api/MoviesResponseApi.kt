package com.dmitriy.movie.model.api

import com.dmitriy.movie.model.data.popularmovie.PopularMovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesResponseApi {

    @GET("3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<PopularMovieList>
}