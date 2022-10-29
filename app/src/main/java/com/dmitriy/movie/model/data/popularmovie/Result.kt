package com.dmitriy.movie.model.data.popularmovie

data class Result(
    val adult: Boolean?, // Рейтинг фильма / категория (для малых или взрослых)
    val backdrop_path: String?,
    val genre_ids: List<Int?>?, // Жанр фильма
    val id: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?, // Дата релиза фильма
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,  // Среднее кол-во голосов / рейтинг фильма
    val vote_count: Int? // Кол-во людский голосов оценивших данный фильм
)