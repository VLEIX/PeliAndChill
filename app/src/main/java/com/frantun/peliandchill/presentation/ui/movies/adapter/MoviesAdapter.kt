package com.frantun.peliandchill.presentation.ui.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.frantun.peliandchill.R
import com.frantun.peliandchill.common.Constants.URL_TMDB_IMAGE_W500
import com.frantun.peliandchill.common.MovieAdapterListener
import com.frantun.peliandchill.databinding.ViewMovieBinding
import com.frantun.peliandchill.databinding.ViewMovieHeaderBinding
import com.frantun.peliandchill.domain.model.Movie

class MoviesAdapter(private val listener: MovieAdapterListener) :
    ListAdapter<Movie, RecyclerView.ViewHolder>(MoviesAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_MOVIE) {
            MovieViewHolder(
                ViewMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            MovieHeaderViewHolder(
                ViewMovieHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder.itemViewType == ITEM_MOVIE) {
            (holder as MovieViewHolder).bind(item, listener)
        } else {
            (holder as MovieHeaderViewHolder).bind(item, listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ITEM_MOVIE_HEADER
        } else {
            ITEM_MOVIE
        }
    }

    class MovieHeaderViewHolder constructor(private val binding: ViewMovieHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie, listener: MovieAdapterListener) {
            binding.apply {
                titleTextView.text = item.title
                val voteAverage = String.format(
                    itemView.context.getString(R.string.vote_average),
                    item.voteAverage
                )
                voteAverageTextView.text = voteAverage
                val posterURL = URL_TMDB_IMAGE_W500 + item.posterPath
                Glide.with(posterImageView.context)
                    .load(posterURL)
                    .transform(CenterCrop())
                    .into(posterImageView)
            }
            itemView.apply {
                animation = AnimationUtils.loadAnimation(itemView.context, R.anim.alpha)
                setOnClickListener {
                    listener.onClick(item)
                }
            }
        }
    }

    class MovieViewHolder constructor(private val binding: ViewMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie, listener: MovieAdapterListener) {
            binding.apply {
                titleTextView.text = item.title
                releaseDateTextView.text = item.releaseDate
                val voteAverage = String.format(
                    itemView.context.getString(R.string.vote_average),
                    item.voteAverage
                )
                voteAverageTextView.text = voteAverage
                val posterURL = URL_TMDB_IMAGE_W500 + item.posterPath
                Glide.with(posterImageView.context)
                    .load(posterURL)
                    .transform(CenterCrop(), RoundedCorners(24))
                    .into(posterImageView)
            }
            itemView.apply {
                animation = AnimationUtils.loadAnimation(itemView.context, R.anim.alpha)
                setOnClickListener {
                    listener.onClick(item)
                }
            }
        }
    }

    companion object {
        const val ITEM_MOVIE_HEADER = 0
        const val ITEM_MOVIE = 1
    }
}

class MoviesAdapterDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.id == newItem.id && oldItem.type == newItem.type
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }
}
