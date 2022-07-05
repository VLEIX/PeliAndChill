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
    operator fun invoke(name: String): Flow<Resource<SeriesDto>> = flow {
        try {
            emit(Resource.Loading<SeriesDto>())
            val moviesResult = seriesRepository.searchSeriesByName(name)
            moviesResult.data?.let {
                emit(Resource.Success<SeriesDto>(it))
            } ?: emit(Resource.Error<SeriesDto>(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error<SeriesDto>(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
