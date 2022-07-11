package com.frantun.peliandchill.presentation.ui.detail.series

import com.frantun.peliandchill.common.BaseCoroutineViewModelStateTest
import com.frantun.peliandchill.common.extensions.assertStateOrder
import com.frantun.peliandchill.common.seriesPopular
import com.frantun.peliandchill.common.seriesPopular1
import com.frantun.peliandchill.common.videos
import com.frantun.peliandchill.data.repository.FakeSeriesRepositoryImpl
import com.frantun.peliandchill.domain.usecase.GetVideosFromSeriesUseCase
import com.frantun.peliandchill.presentation.ui.detail.series.model.SeriesDetailState
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Unit tests for [SeriesDetailViewModel]
 *
 */
@ExperimentalCoroutinesApi
class SeriesDetailViewModelTest : BaseCoroutineViewModelStateTest<SeriesDetailState?>() {

    private lateinit var sut: SeriesDetailViewModel

    override fun before() {
        super.before()

        sut = SeriesDetailViewModel(
            GetVideosFromSeriesUseCase(FakeSeriesRepositoryImpl(seriesPopular(), videos()))
        )
    }

    @Test
    fun `when flow is started, then set state to Initialized`() {
        sut.initState(seriesPopular1())

        assertThat(sut.state.value).isInstanceOf(SeriesDetailState.Initialized::class.java)
    }

    @Test
    fun `when flow is started and videos are retrieved, then set state to RetrievedVideos`() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }
            sut.apply {
                initState(seriesPopular1())
                getVideos()
            }
            stateList.apply {
                removeFirst()
                assertStateOrder(
                    SeriesDetailState.Initialized::class,
                    SeriesDetailState.ShowLoading::class,
                    SeriesDetailState.RetrievedVideos::class
                )
            }
            with(stateList[2] as SeriesDetailState.RetrievedVideos) {
                assertEquals(videos().size, videos.size)
            }

            collectJob.cancel()
        }
}
