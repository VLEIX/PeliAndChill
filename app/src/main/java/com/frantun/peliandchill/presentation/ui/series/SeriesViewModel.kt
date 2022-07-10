package com.frantun.peliandchill.presentation.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.peliandchill.common.Constants
import com.frantun.peliandchill.common.Constants.PAGE_NEGATIVE
import com.frantun.peliandchill.common.Constants.PAGE_ZERO
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.domain.model.SeriesResult
import com.frantun.peliandchill.domain.usecase.GetPopularSeriesUseCase
import com.frantun.peliandchill.domain.usecase.GetTopRatedSeriesUseCase
import com.frantun.peliandchill.presentation.ui.series.model.SeriesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val getPopularSeriesUseCase: GetPopularSeriesUseCase,
    private val getTopRatedSeriesUseCase: GetTopRatedSeriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SeriesState?>(null)
    val state: StateFlow<SeriesState?> get() = _state

    val lastItemIndexFlow = MutableStateFlow<Int?>(null)
    val categoryTypeFlow = MutableStateFlow<Constants.CategoryType?>(null)

    private lateinit var categoryType: Constants.CategoryType
    private var page = PAGE_ZERO
    private var series: List<Series> = emptyList()

    init {
        viewModelScope.launch {
            categoryTypeFlow.collect {
                it?.let {
                    categoryType = it
                    page = PAGE_ZERO

                    _state.value = SeriesState.SelectedCategoryType(categoryType)
                    tryToGetSeries(0)
                }
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

    fun initState() {
        if (!this::categoryType.isInitialized) {
            categoryTypeFlow.value = Constants.CategoryType.TYPE_POPULAR
        } else {
            _state.value = SeriesState.Initialized(categoryType, series)
        }
    }

    private fun tryToGetSeries(lastItemIndex: Int) {
        if (shouldGetSeries(lastItemIndex)) {
            getSeries(page)
        }
    }

    private fun shouldGetSeries(lastItemIndex: Int) =
        (page == PAGE_ZERO) || ((lastItemIndex + 1) >= series.size)

    private fun getSeries(page: Int) {
        if (page != PAGE_NEGATIVE) {
            val nextPage = page + 1
            val resource = when (categoryType) {
                Constants.CategoryType.TYPE_POPULAR -> getPopularSeriesUseCase(nextPage)
                Constants.CategoryType.TYPE_TOP_RATED -> getTopRatedSeriesUseCase(nextPage)
            }

            resource.onEach { result ->
                when (result) {
                    is Resource.Loading -> _state.value = SeriesState.ShowLoading
                    is Resource.Success -> seriesSuccess(result.data)
                    is Resource.Error -> Unit
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun seriesSuccess(seriesResult: SeriesResult?) {
        seriesResult?.let {
            page = it.page
            series = it.series
            _state.value = SeriesState.RetrievedSeries(series)
        }
    }
}
