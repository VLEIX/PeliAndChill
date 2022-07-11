package com.frantun.peliandchill.presentation.ui.series

import com.frantun.peliandchill.common.BaseCoroutineViewModelStateTest
import com.frantun.peliandchill.common.Constants
import com.frantun.peliandchill.common.extensions.assertStateOrder
import com.frantun.peliandchill.common.seriesPopular
import com.frantun.peliandchill.common.seriesTopRated
import com.frantun.peliandchill.common.videos
import com.frantun.peliandchill.data.repository.FakeSeriesRepositoryImpl
import com.frantun.peliandchill.domain.usecase.GetPopularSeriesUseCase
import com.frantun.peliandchill.domain.usecase.GetTopRatedSeriesUseCase
import com.frantun.peliandchill.presentation.ui.series.model.SeriesState
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
 * Unit tests for [SeriesViewModel]
 *
 */
@ExperimentalCoroutinesApi
class SeriesViewModelTest : BaseCoroutineViewModelStateTest<SeriesState?>() {

    private lateinit var sut: SeriesViewModel

    @Before
    fun before() {
        sut = SeriesViewModel(
            GetPopularSeriesUseCase(FakeSeriesRepositoryImpl(seriesPopular(), videos())),
            GetTopRatedSeriesUseCase(FakeSeriesRepositoryImpl(seriesTopRated(), videos()))
        )
    }

    @Test
    fun `when series are retrieved, then set state to RetrievedSeries`() {
        sut.initState()
        assertThat(sut.state.value).isInstanceOf(SeriesState.RetrievedSeries::class.java)
    }

    @Test
    fun `when series are retrieved and category is changed, then set state to RetrievedSeries`() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }
            sut.apply {
                initState()
                categoryTypeFlow.emit(Constants.CategoryType.TYPE_TOP_RATED)
            }
            stateList.apply {
                removeFirst()
                assertStateOrder(
                    SeriesState.SelectedCategoryType::class,
                    SeriesState.ShowLoading::class,
                    SeriesState.RetrievedSeries::class,
                    SeriesState.SelectedCategoryType::class,
                    SeriesState.ShowLoading::class,
                    SeriesState.RetrievedSeries::class
                )
            }
            with(stateList[5] as SeriesState.RetrievedSeries) {
                assertEquals(Constants.TYPE_TOP_RATED, series.first().type)
            }

            collectJob.cancel()
        }

    @Test
    fun `when series are retrieved and list is scrolled, then set state to RetrievedSeries`() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }
            sut.apply {
                initState()
                lastItemIndexFlow.emit(seriesPopular().size)
            }
            stateList.apply {
                removeFirst()
                assertStateOrder(
                    SeriesState.SelectedCategoryType::class,
                    SeriesState.ShowLoading::class,
                    SeriesState.RetrievedSeries::class,
                    SeriesState.ShowLoading::class,
                    SeriesState.RetrievedSeries::class
                )
            }
            with(stateList[4] as SeriesState.RetrievedSeries) {
                assertEquals(Constants.TYPE_POPULAR, series.first().type)
            }

            collectJob.cancel()
        }
}
