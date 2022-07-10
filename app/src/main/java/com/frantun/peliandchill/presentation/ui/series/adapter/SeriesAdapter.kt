package com.frantun.peliandchill.presentation.ui.series.adapter

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
import com.frantun.peliandchill.common.SeriesAdapterListener
import com.frantun.peliandchill.databinding.ViewSeriesBinding
import com.frantun.peliandchill.databinding.ViewSeriesHeaderBinding
import com.frantun.peliandchill.domain.model.Series

class SeriesAdapter(private val listener: SeriesAdapterListener) :
    ListAdapter<Series, RecyclerView.ViewHolder>(SeriesAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SERIES) {
            SeriesViewHolder(
                ViewSeriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            SeriesHeaderViewHolder(
                ViewSeriesHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder.itemViewType == ITEM_SERIES) {
            (holder as SeriesViewHolder).bind(item, listener)
        } else {
            (holder as SeriesHeaderViewHolder).bind(item, listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ITEM_SERIES_HEADER
        } else {
            ITEM_SERIES
        }
    }

    class SeriesHeaderViewHolder constructor(private val binding: ViewSeriesHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Series, listener: SeriesAdapterListener) {
            binding.apply {
                titleTextView.text = item.name
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

    class SeriesViewHolder constructor(private val binding: ViewSeriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Series, listener: SeriesAdapterListener) {
            binding.apply {
                titleTextView.text = item.name
                releaseDateTextView.text = item.firstAirDate
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
        const val ITEM_SERIES_HEADER = 0
        const val ITEM_SERIES = 1
    }
}

class SeriesAdapterDiffCallback : DiffUtil.ItemCallback<Series>() {
    override fun areItemsTheSame(
        oldItem: Series,
        newItem: Series
    ): Boolean {
        return oldItem.id == newItem.id && oldItem.type == newItem.type
    }

    override fun areContentsTheSame(
        oldItem: Series,
        newItem: Series
    ): Boolean {
        return oldItem == newItem
    }
}
