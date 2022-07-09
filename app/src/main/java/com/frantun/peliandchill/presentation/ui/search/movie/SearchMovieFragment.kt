package com.frantun.peliandchill.presentation.ui.search.movie

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frantun.peliandchill.R
import com.frantun.peliandchill.common.MovieAdapterListener
import com.frantun.peliandchill.databinding.FragmentSearchMovieBinding
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.other.hideKeyboard
import com.frantun.peliandchill.other.navigateTo
import com.frantun.peliandchill.other.setAsGone
import com.frantun.peliandchill.other.setAsVisible
import com.frantun.peliandchill.other.showKeyboard
import com.frantun.peliandchill.presentation.common.BaseFragment
import com.frantun.peliandchill.presentation.ui.detail.DetailActivity
import com.frantun.peliandchill.presentation.ui.movies.adapter.MoviesAdapter
import com.frantun.peliandchill.presentation.ui.search.movie.model.SearchMovieState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMovieFragment :
    BaseFragment<FragmentSearchMovieBinding>(FragmentSearchMovieBinding::inflate) {

    private val viewModel: SearchMovieViewModel by viewModels()

    private val moviesAdapter by lazy {
        MoviesAdapter(MovieAdapterListener {
            navigateToMovieDetail(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUi()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    onStateUpdated(state)
                }
            }
        }
    }

    private fun setUi() {
        binding.toolbar.apply {
            title = getString(R.string.title_search_movie)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        var job: Job? = null
        binding.searchEditText.apply {
            addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_TIME_DELAY)
                    editable?.let {
                        viewModel.nameFlow.value = it.toString()
                    }
                }
            }
            setOnEditorActionListener(TextView.OnEditorActionListener { _, _, _ ->
                hideKeyboard()
                return@OnEditorActionListener true
            })
        }

        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesAdapter
            itemAnimator = null
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )

            val layoutManager = layoutManager as LinearLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    hideKeyboard()
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    viewModel.lastItemIndexFlow.value = layoutManager.findLastVisibleItemPosition()
                }
            })
        }
    }

    private fun onStateUpdated(state: SearchMovieState?) {
        when (state) {
            is SearchMovieState.Initialized -> onInitialized(state.movies)
            is SearchMovieState.ShowLoading -> onShowLoading()
            is SearchMovieState.ShowError -> onShowError()
            is SearchMovieState.RetrievedMovies -> onRetrievedMovies(state.movies)
            else -> Unit
        }
    }

    private fun onInitialized(movies: List<Movie>) {
        moviesAdapter.submitList(movies)
        binding.apply {
            progressAnimationView.setAsGone()
            searchingAnimationView.setAsVisible()
            messageTextView.setAsGone()
            showKeyboard(searchEditText)
        }
    }

    private fun onShowLoading() {
        binding.apply {
            progressAnimationView.setAsVisible()
            searchingAnimationView.setAsGone()
            messageTextView.setAsGone()
        }
    }

    private fun onShowError() {
        binding.apply {
            progressAnimationView.setAsGone()
            searchingAnimationView.setAsGone()
            messageTextView.apply {
                setAsVisible()
                text = getString(R.string.error_connection)
            }
        }
    }

    private fun onRetrievedMovies(movies: List<Movie>) {
        binding.apply {
            progressAnimationView.setAsGone()
            searchingAnimationView.setAsGone()
            messageTextView.apply {
                if (movies.isEmpty()) {
                    setAsVisible()
                    text = getString(R.string.no_result_found)
                } else {
                    setAsGone()
                }
            }
        }
        moviesAdapter.submitList(movies)
    }

    private fun navigateToMovieDetail(movie: Movie) {
        navigateTo(DetailActivity.newIntentMovie(requireActivity(), movie))
    }

    companion object {
        private const val SEARCH_TIME_DELAY = 500L
    }
}
