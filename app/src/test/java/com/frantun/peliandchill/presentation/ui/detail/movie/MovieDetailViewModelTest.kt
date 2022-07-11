package com.frantun.peliandchill.presentation.ui.detail.movie

import com.frantun.peliandchill.common.BaseCoroutineViewModelStateTest
import com.frantun.peliandchill.common.extensions.assertStateOrder
import com.frantun.peliandchill.common.moviePopular1
import com.frantun.peliandchill.common.moviesPopular
import com.frantun.peliandchill.common.videos
import com.frantun.peliandchill.data.repository.FakeMoviesRepositoryImpl
import com.frantun.peliandchill.domain.usecase.GetVideosFromMovieUseCase
import com.frantun.peliandchill.presentation.ui.detail.movie.model.MovieDetailState
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Unit tests for [MovieDetailViewModel]
 *
 */
@ExperimentalCoroutinesApi
class MovieDetailViewModelTest : BaseCoroutineViewModelStateTest<MovieDetailState?>() {

    private lateinit var sut: MovieDetailViewModel

    override fun before() {
        super.before()

        sut = MovieDetailViewModel(
            GetVideosFromMovieUseCase(FakeMoviesRepositoryImpl(moviesPopular(), videos()))
        )
    }

    @Test
    fun `when flow is started, then set state to Initialized`() {
        sut.initState(moviePopular1())

        assertThat(sut.state.value).isInstanceOf(MovieDetailState.Initialized::class.java)
    }

    @Test
    fun `when flow is started and videos are retrieved, then set state to RetrievedVideos`() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }
            sut.apply {
                initState(moviePopular1())
                getVideos()
            }
            stateList.apply {
                removeFirst()
                assertStateOrder(
                    MovieDetailState.Initialized::class,
                    MovieDetailState.ShowLoading::class,
                    MovieDetailState.RetrievedVideos::class
                )
            }
            with(stateList[2] as MovieDetailState.RetrievedVideos) {
                assertEquals(videos().size, videos.size)
            }

            collectJob.cancel()
        }
}
