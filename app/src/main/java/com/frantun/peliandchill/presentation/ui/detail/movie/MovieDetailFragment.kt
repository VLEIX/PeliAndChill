package com.frantun.peliandchill.presentation.ui.detail.movie

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
import com.frantun.peliandchill.databinding.FragmentMovieDetailBinding
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.model.Video
import com.frantun.peliandchill.other.setAsGone
import com.frantun.peliandchill.other.setAsInVisible
import com.frantun.peliandchill.other.setAsVisible
import com.frantun.peliandchill.presentation.common.BaseFragment
import com.frantun.peliandchill.presentation.ui.detail.adapter.VideosAdapter
import com.frantun.peliandchill.presentation.ui.detail.movie.model.MovieDetailState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment :
    BaseFragment<FragmentMovieDetailBinding>(FragmentMovieDetailBinding::inflate) {

    private val viewModel: MovieDetailViewModel by viewModels()

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
            with(MovieDetailFragmentArgs.fromBundle(bundle)) {
                viewModel.initState(movie)
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

    private fun onStateUpdated(state: MovieDetailState?) {
        when (state) {
            is MovieDetailState.Initialized -> onInitialized(state.movie)
            is MovieDetailState.ShowLoading -> onShowLoading()
            is MovieDetailState.ShowError -> onShowError()
            is MovieDetailState.RetrievedVideos -> onRetrievedVideos(state.videos)
            else -> Unit
        }
    }

    private fun onInitialized(movie: Movie) {
        binding.apply {
            posterImageView.apply {
                setAsVisible()
                val posterURL = Constants.URL_TMDB_IMAGE_W500 + movie.posterPath
                Glide.with(this@MovieDetailFragment)
                    .load(posterURL)
                    .transform(CenterCrop())
                    .into(this)
            }
            titleTextView.text = movie.title
            dateTextView.text = movie.releaseDate
            val voteAverage = String.format(
                getString(R.string.vote_average),
                movie.voteAverage
            )
            voteAverageTextView.text = voteAverage
            voteCountTextView.text = movie.voteCount.toString()
            voteLanguageView.text = movie.originalLanguage
            overviewTextView.text = movie.overview
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
