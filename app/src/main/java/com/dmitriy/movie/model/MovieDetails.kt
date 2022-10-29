package com.dmitriy.movie.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetails(
    var id: Int,
    var adult: Boolean,
    var genre: List<Int?>,
    var overview: String,
    var releaseDate: String,
    var title: String,
    var voteAverage: Double,
    var voteCount: Int,
    var imageId: String
) : Parcelable