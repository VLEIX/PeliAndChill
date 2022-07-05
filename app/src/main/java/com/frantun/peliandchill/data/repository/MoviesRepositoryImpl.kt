package com.frantun.peliandchill.data.repository

import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Constants.PAGE_ZERO
import com.frantun.peliandchill.common.Constants.TYPE_POPULAR
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.datasource.MoviesLocalDataSource
import com.frantun.peliandchill.data.datasource.MoviesRemoteDataSource
import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import com.frantun.peliandchill.domain.model.MoviesResult
import com.frantun.peliandchill.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesLocalDataSource: MoviesLocalDataSource,
    private val moviesRemoteDataSource: MoviesRemoteDataSource
) : MoviesRepository {

    override suspend fun getPopularMovies(): Resource<MoviesResult> {
        return try {
            when (val result = moviesRemoteDataSource.getPopularMovies()) {
                is Resource.Success -> {
                    getPopularMoviesResultSuccess(result.data)?.let { moviesResult ->
                        Resource.Success(moviesResult)
                    } ?: Resource.Error(ERROR_UNEXPECTED)
                }
                is Resource.Error -> {
                    val moviesLocal = moviesLocalDataSource.getPopularMovies()
                    val moviesResult = MoviesResult(moviesLocal, PAGE_ZERO)
                    Resource.Success(moviesResult)
                }
                else -> Resource.Error(ERROR_UNEXPECTED)
            }
        } catch (exception: Exception) {
            Resource.Error(exception.message ?: exception.toString())
        }
    }

    private suspend fun getPopularMoviesResultSuccess(moviesDto: MoviesDto?): MoviesResult? {
        return moviesDto?.let {
            val moviesRemote = moviesDto.movies.map { movie ->
                movie.apply {
                    type = TYPE_POPULAR
                }
            }
            moviesLocalDataSource.insertMovies(moviesRemote)
            val moviesLocal = moviesLocalDataSource.getPopularMovies()
            MoviesResult(moviesLocal, moviesDto.page)
        }
    }

    override suspend fun getTopRatedMovies(): MoviesDto {
        return moviesRemoteDataSource.getTopRatedMovies()
    }

    override suspend fun getVideosFromMovie(movieId: Int): VideosDto {
        return moviesRemoteDataSource.getVideosFromMovie(movieId = movieId)
    }

    override suspend fun searchMovieByName(name: String): MoviesDto {
        return moviesRemoteDataSource.searchMovieByName(name = name)
    }
}
