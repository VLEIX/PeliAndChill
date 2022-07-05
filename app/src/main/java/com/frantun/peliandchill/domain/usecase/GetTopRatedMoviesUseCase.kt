package com.frantun.peliandchill.domain.usecase

import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.MoviesResult
import com.frantun.peliandchill.domain.repository.MoviesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTopRatedMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    operator fun invoke(): Flow<Resource<MoviesResult>> = flow {
        try {
            emit(Resource.Loading<MoviesResult>())
            val moviesResult = moviesRepository.getTopRatedMovies()
            moviesResult.data?.let {
                emit(Resource.Success<MoviesResult>(it))
            } ?: emit(Resource.Error<MoviesResult>(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error<MoviesResult>(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
