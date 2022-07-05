package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.common.BaseRemoteDataSource
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.MoviesApi
import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val moviesApi: MoviesApi
) : BaseRemoteDataSource(), MoviesRemoteDataSource {

    override suspend fun getPopularMovies(): Resource<MoviesDto> = getResult {
        moviesApi.getPopularMovies()
    }

    override suspend fun getTopRatedMovies(): Resource<MoviesDto> = getResult {
        moviesApi.getTopRatedMovies()
    }

    override suspend fun getVideosFromMovie(movieId: Int): Resource<VideosDto> = getResult {
        moviesApi.getVideosFromMovie(movieId = movieId)
    }

    override suspend fun searchMovieByName(name: String): Resource<MoviesDto> = getResult {
        moviesApi.searchMovieByName(name = name)
    }
}