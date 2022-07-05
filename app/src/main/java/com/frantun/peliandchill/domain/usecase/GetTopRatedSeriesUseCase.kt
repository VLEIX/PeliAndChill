package com.frantun.peliandchill.domain.usecase

import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.SeriesResult
import com.frantun.peliandchill.domain.repository.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTopRatedSeriesUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {
    operator fun invoke(): Flow<Resource<SeriesResult>> = flow {
        try {
            emit(Resource.Loading<SeriesResult>())
            val moviesResult = seriesRepository.getTopRatedSeries()
            moviesResult.data?.let {
                emit(Resource.Success<SeriesResult>(it))
            } ?: emit(Resource.Error<SeriesResult>(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error<SeriesResult>(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
