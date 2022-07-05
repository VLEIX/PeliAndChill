package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.common.Constants.TYPE_POPULAR
import com.frantun.peliandchill.common.Constants.TYPE_TOP_RATED
import com.frantun.peliandchill.data.local.dao.SeriesDao
import com.frantun.peliandchill.domain.model.Series
import javax.inject.Inject

class SeriesLocalDataSourceImpl @Inject constructor(
    private val seriesDao: SeriesDao
) : SeriesLocalDataSource {

    override suspend fun insertSeries(series: List<Series>) {
        seriesDao.insertSeries(series)
    }

    override suspend fun getPopularSeries(): List<Series> {
        return seriesDao.getSeriesByType(TYPE_POPULAR)
    }

    override suspend fun getTopRatedSeries(): List<Series> {
        return seriesDao.getSeriesByType(TYPE_TOP_RATED)
    }
}
