package com.dmitriy.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmitriy.movie.model.data.popularmovie.PopularMovieList
import com.dmitriy.movie.model.data.popularmovie.Result
import com.dmitriy.movie.repository.popularmovies.PopularMoviesRepository
import com.dmitriy.movie.repository.popularmovies.PopularMoviesRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel : ViewModel() {

    private val _popularMovies = MutableLiveData<List<Result?>?>()
    val popularMovies: LiveData<List<Result?>?> = _popularMovies

    private val popularMoviesRepository: PopularMoviesRepository = PopularMoviesRepositoryImpl()

    fun getPopularMovies() {
        val response = popularMoviesRepository.getPopularMovies()
        response.enqueue(object : Callback<PopularMovieList> {
            override fun onResponse(
                call: Call<PopularMovieList>,
                response: Response<PopularMovieList>
            ) {
                _popularMovies.postValue(response.body()?.results)
                Log.d("logs", "OnResponse")
                //_popularMovies.value = response.body()?.results
            }

            override fun onFailure(call: Call<PopularMovieList>, t: Throwable) {
                _popularMovies.postValue(null)
                //_popularMovies.value = null
                Log.d("logs", "onFailure: ${t.message}")
            }
        })
    }
}