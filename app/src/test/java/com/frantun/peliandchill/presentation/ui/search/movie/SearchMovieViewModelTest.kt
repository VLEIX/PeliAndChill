package com.frantun.peliandchill.presentation.ui.search.movie

import com.frantun.peliandchill.common.BaseCoroutineViewModelStateTest
import com.frantun.peliandchill.common.extensions.assertStateOrder
import com.frantun.peliandchill.common.moviesPopular
import com.frantun.peliandchill.common.videos
import com.frantun.peliandchill.data.repository.FakeMoviesRepositoryImpl
import com.frantun.peliandchill.domain.usecase.SearchMovieByNameUseCase
import com.frantun.peliandchill.presentation.ui.search.movie.model.SearchMovieState
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
 * Unit tests for [SearchMovieViewModel]
 *
 */
@ExperimentalCoroutinesApi
class SearchMovieViewModelTest : BaseCoroutineViewModelStateTest<SearchMovieState?>() {

    private lateinit var sut: SearchMovieViewModel

    @Before
    fun before() {
        sut = SearchMovieViewModel(
            SearchMovieByNameUseCase(FakeMoviesRepositoryImpl(moviesPopular(), videos()))
        )
    }

    @Test
    fun `when name is empty, then set state to Initialized`() {
        assertThat(sut.state.value).isInstanceOf(SearchMovieState.Initialized::class.java)
    }

    @Test
    fun `when name is filled, then set state to RetrievedMovies`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            sut.state.toList(stateList)
        }
        sut.nameFlow.emit(MOVIE_NAME)
        stateList.apply {
            assertStateOrder(
                SearchMovieState.Initialized::class,
                SearchMovieState.ShowLoading::class,
                SearchMovieState.RetrievedMovies::class
            )
        }
        with(stateList[2] as SearchMovieState.RetrievedMovies) {
            assertEquals(moviesPopular().size, movies.size)
        }

        collectJob.cancel()
    }

    @Test
    fun `when name is filled and list is scrolled, then set state to RetrievedMovies`() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            sut.state.toList(stateList)
        }
        sut.apply {
            nameFlow.emit(MOVIE_NAME)
            lastItemIndexFlow.emit(moviesPopular().size)
        }
        stateList.apply {
            assertStateOrder(
                SearchMovieState.Initialized::class,
                SearchMovieState.ShowLoading::class,
                SearchMovieState.RetrievedMovies::class,
                SearchMovieState.ShowLoading::class,
                SearchMovieState.RetrievedMovies::class
            )
        }

        collectJob.cancel()
    }

    companion object {
        private const val MOVIE_NAME = "MOVIE_NAME"
    }
}
