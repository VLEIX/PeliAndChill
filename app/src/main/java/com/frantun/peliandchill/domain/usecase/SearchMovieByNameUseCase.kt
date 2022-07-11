package com.frantun.peliandchill.domain.usecase

import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.domain.repository.MoviesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchMovieByNameUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    operator fun invoke(name: String, page: Int): Flow<Resource<MoviesDto>> = flow {
        try {
            emit(Resource.Loading())
            val moviesResult = moviesRepository.searchMovieByName(name, page)
            moviesResult.data?.let {
                emit(Resource.Success(it))
            } ?: emit(Resource.Error(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
