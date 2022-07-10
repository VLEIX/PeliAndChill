package com.frantun.peliandchill.presentation.ui.search.series.model

import com.frantun.peliandchill.domain.model.Series

sealed class SearchSeriesState {
    data class Initialized(val series: List<Series>) : SearchSeriesState()
    object ShowLoading : SearchSeriesState()
    object ShowError : SearchSeriesState()
    data class RetrievedSeries(val series: List<Series>) : SearchSeriesState()
}
