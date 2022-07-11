package com.frantun.peliandchill.presentation.ui.search.series

import com.frantun.peliandchill.common.BaseCoroutineViewModelStateTest
import com.frantun.peliandchill.common.extensions.assertStateOrder
import com.frantun.peliandchill.common.seriesPopular
import com.frantun.peliandchill.common.videos
import com.frantun.peliandchill.data.repository.FakeSeriesRepositoryImpl
import com.frantun.peliandchill.domain.usecase.SearchSeriesByNameUseCase
import com.frantun.peliandchill.presentation.ui.search.series.model.SearchSeriesState
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [SearchSeriesViewModel]
 *
 */
@ExperimentalCoroutinesApi
class SearchSeriesViewModelTest : BaseCoroutineViewModelStateTest<SearchSeriesState?>() {

    private lateinit var sut: SearchSeriesViewModel

    @Before
    fun before() {
        sut = SearchSeriesViewModel(
            SearchSeriesByNameUseCase(FakeSeriesRepositoryImpl(seriesPopular(), videos()))
        )
    }

    @Test
    fun `when name is empty, then set state to Initialized`() {
        assertThat(sut.state.value).isInstanceOf(SearchSeriesState.Initialized::class.java)
    }

    @Test
    fun `when name is filled, then set state to RetrievedSeries`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            sut.state.toList(stateList)
        }
        sut.nameFlow.emit(SERIES_NAME)
        stateList.apply {
            assertStateOrder(
                SearchSeriesState.Initialized::class,
                SearchSeriesState.ShowLoading::class,
                SearchSeriesState.RetrievedSeries::class
            )
        }
        with(stateList[2] as SearchSeriesState.RetrievedSeries) {
            assertEquals(seriesPopular().size, series.size)
        }

        collectJob.cancel()
    }

    @Test
    fun `when name is filled and list is scrolled, then set state to RetrievedSeries`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            sut.state.toList(stateList)
        }
        sut.apply {
            nameFlow.emit(SERIES_NAME)
            lastItemIndexFlow.emit(seriesPopular().size)
        }
        stateList.apply {
            assertStateOrder(
                SearchSeriesState.Initialized::class,
                SearchSeriesState.ShowLoading::class,
                SearchSeriesState.RetrievedSeries::class,
                SearchSeriesState.ShowLoading::class,
                SearchSeriesState.RetrievedSeries::class
            )
        }

        collectJob.cancel()
    }

    companion object {
        private const val SERIES_NAME = "SERIES_NAME"
    }
}
