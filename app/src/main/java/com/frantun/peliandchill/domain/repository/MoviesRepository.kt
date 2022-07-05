package com.frantun.peliandchill.domain.repository

import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import com.frantun.peliandchill.domain.model.MoviesResult

interface MoviesRepository {
    suspend fun getPopularMovies(): Resource<MoviesResult>
    suspend fun getTopRatedMovies(): Resource<MoviesResult>
    suspend fun getVideosFromMovie(movieId: Int): Resource<VideosDto>
    suspend fun searchMovieByName(name: String): Resource<MoviesDto>
}
