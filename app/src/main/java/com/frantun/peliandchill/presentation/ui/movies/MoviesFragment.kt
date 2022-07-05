package com.frantun.peliandchill.presentation.ui.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frantun.peliandchill.databinding.FragmentMoviesBinding
import com.frantun.peliandchill.presentation.common.BaseFragment
import com.frantun.peliandchill.presentation.ui.movies.adapter.MoviesAdapter
import com.frantun.peliandchill.presentation.ui.movies.model.MoviesState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {

    private val viewModel: MoviesViewModel by viewModels()

    private val moviesAdapter by lazy { MoviesAdapter() }

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
        binding.moviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moviesAdapter
            itemAnimator = null

            val layoutManager = layoutManager as LinearLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    viewModel.lastVisible.value = layoutManager.findLastVisibleItemPosition()
                }
            })
        }
    }

    private fun onStateUpdated(state: MoviesState?) {
        when (state) {
            is MoviesState.RetrievedMovies -> {
                moviesAdapter.submitList(state.movies)
            }
            else -> Unit
        }
    }
}
