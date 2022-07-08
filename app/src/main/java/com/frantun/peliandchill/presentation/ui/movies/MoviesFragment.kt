package com.frantun.peliandchill.presentation.ui.movies

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frantun.peliandchill.R
import com.frantun.peliandchill.common.Constants
import com.frantun.peliandchill.common.ItemAdapterListener
import com.frantun.peliandchill.databinding.FragmentMoviesBinding
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.other.navigateTo
import com.frantun.peliandchill.other.setAsGone
import com.frantun.peliandchill.other.setAsVisible
import com.frantun.peliandchill.presentation.common.BaseFragment
import com.frantun.peliandchill.presentation.ui.movies.adapter.MoviesAdapter
import com.frantun.peliandchill.presentation.ui.movies.model.MoviesState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {

    private val viewModel: MoviesViewModel by viewModels()

    private val moviesAdapter by lazy {
        MoviesAdapter(ItemAdapterListener {

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
            title = getString(R.string.title_movies)
            addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.search_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_search -> {
                            navigateToSearchMovie()
                            true
                        }
                        else -> false
                    }
                }
            })
        }

        binding.tabBar.apply {
            tabItem1.apply {
                setOnClickListener {
                    selectedTabItem.animate().x(0F).duration = 100

                    viewModel.categoryTypeFlow.value = Constants.CategoryType.TYPE_POPULAR
                }
                text = getString(R.string.type_popular)
            }
            tabItem2.apply {
                setOnClickListener {
                    val tabItem2Width = tabItem2.width
                    selectedTabItem.animate().x(tabItem2Width.toFloat()).duration = 100

                    viewModel.categoryTypeFlow.value = Constants.CategoryType.TYPE_TOP_RATED
                }
                text = getString(R.string.type_top_rated)
            }
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
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    viewModel.lastItemIndexFlow.value = layoutManager.findLastVisibleItemPosition()
                }
            })
        }
    }

    private fun onStateUpdated(state: MoviesState?) {
        when (state) {
            is MoviesState.SelectedCategoryType -> onSelectedCategoryType()
            is MoviesState.ShowLoading -> onShowLoading()
            is MoviesState.RetrievedMovies -> onRetrievedMovies(state.movies)
            else -> Unit
        }
    }

    private fun onSelectedCategoryType() {
        viewModel.lastItemIndexFlow.value = 0
    }

    private fun onShowLoading() {
        binding.progressAnimationView.setAsVisible()
    }

    private fun onRetrievedMovies(movies: List<Movie>) {
        binding.progressAnimationView.setAsGone()
        moviesAdapter.submitList(movies)
    }

    private fun navigateToSearchMovie() {
        val action = MoviesFragmentDirections.moviesToSearchMovieAction()
        navigateTo(action)
    }
}
