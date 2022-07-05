package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto

interface MoviesRemoteDataSource {
    suspend fun getPopularMovies(): Resource<MoviesDto>
    suspend fun getTopRatedMovies(): Resource<MoviesDto>
    suspend fun getVideosFromMovie(movieId: Int): Resource<VideosDto>
    suspend fun searchMovieByName(name: String): Resource<MoviesDto>
}
