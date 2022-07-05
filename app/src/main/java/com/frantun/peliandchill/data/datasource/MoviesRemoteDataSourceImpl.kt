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

    override suspend fun getPopularMovies(page: Int): Resource<MoviesDto> = getResult {
        moviesApi.getPopularMovies(page = page)
    }

    override suspend fun getTopRatedMovies(page: Int): Resource<MoviesDto> = getResult {
        moviesApi.getTopRatedMovies(page = page)
    }

    override suspend fun getVideosFromMovie(movieId: Int): Resource<VideosDto> = getResult {
        moviesApi.getVideosFromMovie(movieId = movieId)
    }

    override suspend fun searchMovieByName(name: String, page: Int): Resource<MoviesDto> =
        getResult {
            moviesApi.searchMovieByName(name = name, page = page)
        }
}
