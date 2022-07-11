package com.frantun.peliandchill.presentation.ui.detail.series

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.frantun.peliandchill.R
import com.frantun.peliandchill.common.Constants
import com.frantun.peliandchill.common.MarginItemHorizontalDecoration
import com.frantun.peliandchill.common.VideoAdapterListener
import com.frantun.peliandchill.databinding.FragmentSeriesDetailBinding
import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.domain.model.Video
import com.frantun.peliandchill.other.setAsGone
import com.frantun.peliandchill.other.setAsInVisible
import com.frantun.peliandchill.other.setAsVisible
import com.frantun.peliandchill.presentation.common.BaseFragment
import com.frantun.peliandchill.presentation.ui.detail.adapter.VideosAdapter
import com.frantun.peliandchill.presentation.ui.detail.series.model.SeriesDetailState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeriesDetailFragment :
    BaseFragment<FragmentSeriesDetailBinding>(FragmentSeriesDetailBinding::inflate) {

    private val viewModel: SeriesDetailViewModel by viewModels()

    private val videosAdapter by lazy {
        VideosAdapter(VideoAdapterListener {
            playYoutubeVideo(it.key)
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

        arguments?.let { bundle ->
            with(SeriesDetailFragmentArgs.fromBundle(bundle)) {
                viewModel.initState(series)
            }
        }
    }

    private fun setUi() {
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        binding.videosRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = videosAdapter
            itemAnimator = null
            addItemDecoration(
                MarginItemHorizontalDecoration(
                    resources.getDimension(R.dimen.dimen_4dp).toInt()
                )
            )
        }
    }

    private fun onStateUpdated(state: SeriesDetailState?) {
        when (state) {
            is SeriesDetailState.Initialized -> onInitialized(state.Series)
            is SeriesDetailState.ShowLoading -> onShowLoading()
            is SeriesDetailState.ShowError -> onShowError()
            is SeriesDetailState.RetrievedVideos -> onRetrievedVideos(state.videos)
            else -> Unit
        }
    }

    private fun onInitialized(series: Series) {
        binding.apply {
            posterImageView.apply {
                setAsVisible()
                val posterURL = Constants.URL_TMDB_IMAGE_W500 + series.posterPath
                Glide.with(this@SeriesDetailFragment)
                    .load(posterURL)
                    .transform(CenterCrop())
                    .into(this)
            }
            titleTextView.text = series.name
            dateTextView.text = series.firstAirDate
            val voteAverage = String.format(
                getString(R.string.vote_average),
                series.voteAverage
            )
            voteAverageTextView.text = voteAverage
            voteCountTextView.text = series.voteCount.toString()
            voteLanguageView.text = series.originalLanguage
            overviewTextView.text = series.overview
        }

        viewModel.getVideos()
    }

    private fun onShowLoading() {
        binding.apply {
            videosProgressBar.setAsVisible()
            videosRecyclerView.setAsInVisible()
            videoPlayerView.setAsInVisible()
            posterImageView.setAsVisible()
        }
    }

    private fun onShowError() {
        hideVideosAndVideoPlayer()
    }

    private fun onRetrievedVideos(videos: List<Video>) {
        binding.apply {
            if (videos.isNotEmpty()) {
                videosProgressBar.setAsGone()
                videosRecyclerView.setAsVisible()
                videoPlayerView.setAsVisible()
                posterImageView.setAsGone()
                videosAdapter.submitList(videos)

                playYoutubeVideo(videos.first().key)
            } else {
                hideVideosAndVideoPlayer()
            }
        }
    }

    private fun hideVideosAndVideoPlayer() {
        binding.apply {
            videosLabelTextView.setAsGone()
            videosProgressBar.setAsGone()
            videosRecyclerView.setAsGone()
            videoPlayerView.setAsInVisible()
            posterImageView.setAsVisible()
        }
    }

    private fun playYoutubeVideo(videoKey: String) {
        binding.videoPlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoKey, 0F)
            }
        })
    }
}
