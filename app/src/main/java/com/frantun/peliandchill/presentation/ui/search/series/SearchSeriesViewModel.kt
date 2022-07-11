package com.frantun.peliandchill.presentation.ui.search.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.peliandchill.common.Constants.PAGE_ZERO
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.domain.usecase.SearchSeriesByNameUseCase
import com.frantun.peliandchill.presentation.ui.search.series.model.SearchSeriesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class SearchSeriesViewModel @Inject constructor(
    private val searchSearchSearchSeriesByNameUseCase: SearchSeriesByNameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SearchSeriesState?>(null)
    val state: StateFlow<SearchSeriesState?> get() = _state

    val lastItemIndexFlow = MutableStateFlow<Int?>(null)
    val nameFlow = MutableStateFlow("")

    private var name = ""
    private var page: Int = PAGE_ZERO
    private var series: List<Series> = emptyList()

    init {
        viewModelScope.launch {
            nameFlow.collect {
                name = it
                page = PAGE_ZERO
                series = emptyList()

                tryToGetSeries(0)
            }
        }

        viewModelScope.launch {
            lastItemIndexFlow.collect {
                it?.let {
                    tryToGetSeries(it)
                }
            }
        }
    }

    private fun tryToGetSeries(lastItemIndex: Int) {
        if (name.isEmpty()) {
            page = PAGE_ZERO
            series = emptyList()
            _state.value = SearchSeriesState.Initialized(series)
        } else if (shouldGetSeries(lastItemIndex)) {
            getSeries(page)
        }
    }

    private fun shouldGetSeries(lastItemIndex: Int) =
        (page == PAGE_ZERO) || ((lastItemIndex + 1) >= series.size)

    private fun getSeries(page: Int) {
        val nextPage = page + 1
        val resource = searchSearchSearchSeriesByNameUseCase(name, nextPage)

        resource.onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = SearchSeriesState.ShowLoading
                is Resource.Success -> seriesSuccess(result.data)
                is Resource.Error -> _state.value = SearchSeriesState.ShowError
            }
        }.launchIn(viewModelScope)
    }

    private fun seriesSuccess(seriesDto: SeriesDto?) {
        seriesDto?.let {
            page = it.page
            series = series + it.series
            _state.value = SearchSeriesState.RetrievedSeries(series)
        }
    }
}
