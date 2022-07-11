package com.frantun.peliandchill.data.repository

import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.model.MoviesResult
import com.frantun.peliandchill.domain.model.Video
import com.frantun.peliandchill.domain.repository.MoviesRepository

class FakeMoviesRepositoryImpl(
    private val movies: List<Movie>,
    private val videos: List<Video>
) : MoviesRepository {
    override suspend fun getPopularMovies(page: Int): Resource<MoviesResult> {
        return Resource.Success(MoviesResult(movies, page))
    }

    override suspend fun getTopRatedMovies(page: Int): Resource<MoviesResult> {
        return Resource.Success(MoviesResult(movies, page))
    }

    override suspend fun getVideosFromMovie(movieId: String): Resource<VideosDto> {
        return Resource.Success(VideosDto(videos))
    }

    override suspend fun searchMovieByName(name: String, page: Int): Resource<MoviesDto> {
        return Resource.Success(MoviesDto(page, movies))
    }
}
