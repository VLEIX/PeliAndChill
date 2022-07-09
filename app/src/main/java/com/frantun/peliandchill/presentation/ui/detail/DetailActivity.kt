package com.frantun.peliandchill.presentation.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.frantun.peliandchill.R
import com.frantun.peliandchill.databinding.ActivityDetailBinding
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.presentation.common.BaseActivity
import com.frantun.peliandchill.presentation.ui.detail.movie.MovieDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        determineDestination()
    }

    override fun finish() {
        super.finish()

        overridePendingTransition(R.anim.nothing, R.anim.slide_out_down)
    }

    private fun determineDestination() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        val navController = navHostFragment.findNavController()
        val navGraph = navController.navInflater.inflate(R.navigation.detail_navigation)

        when (getEditFlow()) {
            FlowType.MOVIE -> {
                navGraph.setStartDestination(R.id.navigation_movie_detail)
                getMovie()?.let { movie ->
                    val args = MovieDetailFragmentArgs(movie)
                    navController.setGraph(navGraph, args.toBundle())
                }
            }
            FlowType.SERIES -> {
//                navGraph.setStartDestination(R.id.consult_dest)
//
//                val args = ConsultFragmentArgs(getBranchSelected(), getLocationSelected())
//                navController.setGraph(navGraph, args.toBundle())
            }
            else -> Unit
        }
    }

    private fun getEditFlow() = intent.getSerializableExtra(KEY_FLOW_TYPE) as? FlowType
    private fun getMovie() = intent.getParcelableExtra(KEY_MOVIE) as? Movie
    private fun getSeries() = intent.getParcelableExtra(KEY_SERIES) as? Series

    companion object {

        private const val KEY_FLOW_TYPE = "KEY_FLOW_TYPE"
        private const val KEY_MOVIE = "KEY_MOVIE"
        private const val KEY_SERIES = "KEY_SERIES"

        private fun newIntent(context: Context) = Intent(context, DetailActivity::class.java)

        fun newIntentMovie(context: Context, movie: Movie) = newIntent(context).apply {
            putExtra(KEY_FLOW_TYPE, FlowType.MOVIE)
            putExtra(KEY_MOVIE, movie)
        }

        fun newIntentSeries(context: Context, series: Series) = newIntent(context).apply {
            putExtra(KEY_FLOW_TYPE, FlowType.SERIES)
            putExtra(KEY_SERIES, series)
        }

        enum class FlowType {
            MOVIE,
            SERIES
        }
    }
}
