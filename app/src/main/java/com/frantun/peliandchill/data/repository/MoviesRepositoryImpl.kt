package com.frantun.peliandchill.data.repository

import com.frantun.peliandchill.data.remote.MoviesApi
import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import com.frantun.peliandchill.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi
) : MoviesRepository {

    override suspend fun getPopularMovies(): MoviesDto {
        return moviesApi.getPopularMovies()
    }

    override suspend fun getTopRatedMovies(): MoviesDto {
        return moviesApi.getTopRatedMovies()
    }

    override suspend fun getVideosFromMovie(movieId: Int): VideosDto {
        return moviesApi.getVideosFromMovie(movieId = movieId)
    }

    override suspend fun searchMovieByName(name: String): MoviesDto {
        return moviesApi.searchMovieByName(name = name)
    }
}
