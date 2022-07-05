package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.common.Constants.TYPE_POPULAR
import com.frantun.peliandchill.common.Constants.TYPE_TOP_RATED
import com.frantun.peliandchill.data.local.dao.MovieDao
import com.frantun.peliandchill.domain.model.Movie
import javax.inject.Inject

class MoviesLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao
) : MoviesLocalDataSource {

    override suspend fun insertMovies(movies: List<Movie>) {
        movieDao.insertMovies(movies)
    }

    override suspend fun getPopularMovies(): List<Movie> {
        return movieDao.getMoviesByType(TYPE_POPULAR)
    }

    override suspend fun getTopRatedMovies(): List<Movie> {
        return movieDao.getMoviesByType(TYPE_TOP_RATED)
    }
}
