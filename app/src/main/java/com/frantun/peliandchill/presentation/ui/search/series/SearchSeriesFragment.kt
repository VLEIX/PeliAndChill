package com.frantun.peliandchill.presentation.ui.search.series

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
import com.frantun.peliandchill.common.SeriesAdapterListener
import com.frantun.peliandchill.databinding.FragmentSearchSeriesBinding
import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.other.hideKeyboard
import com.frantun.peliandchill.other.navigateTo
import com.frantun.peliandchill.other.setAsGone
import com.frantun.peliandchill.other.setAsVisible
import com.frantun.peliandchill.other.showKeyboard
import com.frantun.peliandchill.presentation.common.BaseFragment
import com.frantun.peliandchill.presentation.ui.detail.DetailActivity
import com.frantun.peliandchill.presentation.ui.search.series.model.SearchSeriesState
import com.frantun.peliandchill.presentation.ui.series.adapter.SeriesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchSeriesFragment :
    BaseFragment<FragmentSearchSeriesBinding>(FragmentSearchSeriesBinding::inflate) {

    private val viewModel: SearchSeriesViewModel by viewModels()

    private val seriesAdapter by lazy {
        SeriesAdapter(SeriesAdapterListener {
            navigateToSeriesDetail(it)
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
            title = getString(R.string.title_search_series)
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

        binding.seriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = seriesAdapter
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

    private fun onStateUpdated(state: SearchSeriesState?) {
        when (state) {
            is SearchSeriesState.Initialized -> onInitialized(state.series)
            is SearchSeriesState.ShowLoading -> onShowLoading()
            is SearchSeriesState.ShowError -> onShowError()
            is SearchSeriesState.RetrievedSeries -> onRetrievedSeries(state.series)
            else -> Unit
        }
    }

    private fun onInitialized(series: List<Series>) {
        seriesAdapter.submitList(series)
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

    private fun onRetrievedSeries(series: List<Series>) {
        binding.apply {
            progressAnimationView.setAsGone()
            searchingAnimationView.setAsGone()
            messageTextView.apply {
                if (series.isEmpty()) {
                    setAsVisible()
                    text = getString(R.string.no_result_found)
                } else {
                    setAsGone()
                }
            }
        }
        seriesAdapter.submitList(series)
    }

    private fun navigateToSeriesDetail(series: Series) {
        navigateTo(DetailActivity.newIntentSeries(requireActivity(), series))
    }

    companion object {
        private const val SEARCH_TIME_DELAY = 500L
    }
}
