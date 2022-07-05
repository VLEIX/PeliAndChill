package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.domain.model.Movie

interface MoviesLocalDataSource {
    suspend fun insertMovies(movies: List<Movie>)
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getTopRatedMovies(): List<Movie>
}
