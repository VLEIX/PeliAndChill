package com.frantun.peliandchill.presentation.ui.series.model

import com.frantun.peliandchill.common.Constants
import com.frantun.peliandchill.domain.model.Series

sealed class SeriesState {
    object ShowLoading : SeriesState()
    data class SelectedCategoryType(val categoryType: Constants.CategoryType) : SeriesState()
    data class RetrievedSeries(val series: List<Series>) : SeriesState()
    data class Initialized(val categoryType: Constants.CategoryType, val series: List<Series>) : SeriesState()
}
