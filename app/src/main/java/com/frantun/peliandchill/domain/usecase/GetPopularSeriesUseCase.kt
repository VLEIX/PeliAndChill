package com.frantun.peliandchill.domain.usecase

import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.SeriesResult
import com.frantun.peliandchill.domain.repository.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPopularSeriesUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {
    operator fun invoke(page: Int): Flow<Resource<SeriesResult>> = flow {
        try {
            emit(Resource.Loading())
            val moviesResult = seriesRepository.getPopularSeries(page)
            moviesResult.data?.let {
                emit(Resource.Success(it))
            } ?: emit(Resource.Error(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
