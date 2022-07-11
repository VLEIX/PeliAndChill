package com.frantun.peliandchill.presentation.ui.movies

import com.frantun.peliandchill.common.BaseCoroutineViewModelStateTest
import com.frantun.peliandchill.common.Constants
import com.frantun.peliandchill.common.extensions.assertStateOrder
import com.frantun.peliandchill.common.moviesPopular
import com.frantun.peliandchill.common.moviesTopRated
import com.frantun.peliandchill.common.videos
import com.frantun.peliandchill.data.repository.FakeMoviesRepositoryImpl
import com.frantun.peliandchill.domain.usecase.GetPopularMoviesUseCase
import com.frantun.peliandchill.domain.usecase.GetTopRatedMoviesUseCase
import com.frantun.peliandchill.presentation.ui.movies.model.MoviesState
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
 * Unit tests for [MoviesViewModel]
 *
 */
@ExperimentalCoroutinesApi
class MoviesViewModelTest : BaseCoroutineViewModelStateTest<MoviesState?>() {

    private lateinit var sut: MoviesViewModel

    @Before
    fun before() {
        sut = MoviesViewModel(
            GetPopularMoviesUseCase(FakeMoviesRepositoryImpl(moviesPopular(), videos())),
            GetTopRatedMoviesUseCase(FakeMoviesRepositoryImpl(moviesTopRated(), videos()))
        )
    }

    @Test
    fun `when movies are retrieved, then set state to RetrievedMovies`() {
        sut.initState()
        assertThat(sut.state.value).isInstanceOf(MoviesState.RetrievedMovies::class.java)
    }

    @Test
    fun `when movies are retrieved and category is changed, then set state to RetrievedMovies`() =
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
                    MoviesState.SelectedCategoryType::class,
                    MoviesState.ShowLoading::class,
                    MoviesState.RetrievedMovies::class,
                    MoviesState.SelectedCategoryType::class,
                    MoviesState.ShowLoading::class,
                    MoviesState.RetrievedMovies::class
                )
            }
            with(stateList[5] as MoviesState.RetrievedMovies) {
                assertEquals(Constants.TYPE_TOP_RATED, movies.first().type)
            }

            collectJob.cancel()
        }

    @Test
    fun `when movies are retrieved and list is scrolled, then set state to RetrievedMovies`() =
        runTest {
            val collectJob = launch(UnconfinedTestDispatcher()) {
                sut.state.toList(stateList)
            }
            sut.apply {
                initState()
                lastItemIndexFlow.emit(moviesPopular().size)
            }
            stateList.apply {
                removeFirst()
                assertStateOrder(
                    MoviesState.SelectedCategoryType::class,
                    MoviesState.ShowLoading::class,
                    MoviesState.RetrievedMovies::class,
                    MoviesState.ShowLoading::class,
                    MoviesState.RetrievedMovies::class
                )
            }
            with(stateList[4] as MoviesState.RetrievedMovies) {
                assertEquals(Constants.TYPE_POPULAR, movies.first().type)
            }

            collectJob.cancel()
        }
}
