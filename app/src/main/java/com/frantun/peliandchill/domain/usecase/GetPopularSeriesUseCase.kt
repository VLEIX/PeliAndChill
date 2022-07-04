package com.frantun.peliandchill.domain.usecase

import com.frantun.peliandchill.common.Constants.ERROR_CONNECTION
import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.domain.repository.SeriesRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetPopularSeriesUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {
    operator fun invoke(): Flow<Resource<List<Series>>> = flow {
        try {
            emit(Resource.Loading<List<Series>>())
            val series = seriesRepository.getPopularSeries().series
            emit(Resource.Success<List<Series>>(series))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Series>>(e.localizedMessage ?: ERROR_UNEXPECTED))
        } catch (e: IOException) {
            emit(Resource.Error<List<Series>>(ERROR_CONNECTION))
        }
    }
}
