package com.dmitriy.movie.model.data.popularmovie

data class PopularMovieList(
    val page: Int?,
    val results: List<Result?>?,
    val total_pages: Int?,
    val total_results: Int?
)