package com.frantun.peliandchill.domain.usecase

import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.domain.repository.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchSeriesByNameUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {
    operator fun invoke(name: String, page: Int): Flow<Resource<SeriesDto>> = flow {
        try {
            emit(Resource.Loading())
            val moviesResult = seriesRepository.searchSeriesByName(name, page)
            moviesResult.data?.let {
                emit(Resource.Success(it))
            } ?: emit(Resource.Error(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
