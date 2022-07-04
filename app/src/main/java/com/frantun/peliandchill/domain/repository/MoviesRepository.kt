package com.frantun.peliandchill.domain.repository

import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto

interface MoviesRepository {
    suspend fun getPopularMovies(): MoviesDto
    suspend fun getTopRatedMovies(): MoviesDto
    suspend fun getVideosFromMovie(movieId: Int): VideosDto
    suspend fun searchMovieByName(name: String): MoviesDto
}
