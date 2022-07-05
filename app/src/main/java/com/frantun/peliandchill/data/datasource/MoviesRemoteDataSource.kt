package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto

interface MoviesRemoteDataSource {
    suspend fun getPopularMovies(page: Int): Resource<MoviesDto>
    suspend fun getTopRatedMovies(page: Int): Resource<MoviesDto>
    suspend fun getVideosFromMovie(movieId: Int): Resource<VideosDto>
    suspend fun searchMovieByName(name: String, page: Int): Resource<MoviesDto>
}
