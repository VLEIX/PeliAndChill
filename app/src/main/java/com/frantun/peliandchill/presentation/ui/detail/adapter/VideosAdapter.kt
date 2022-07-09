package com.frantun.peliandchill.presentation.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.frantun.peliandchill.common.Constants.URL_TMDB_IMAGE_W500
import com.frantun.peliandchill.common.VideoAdapterListener
import com.frantun.peliandchill.databinding.ViewVideoBinding
import com.frantun.peliandchill.domain.model.Video

class VideosAdapter(private val listener: VideoAdapterListener) :
    ListAdapter<Video, VideosAdapter.ViewHolder>(VideosAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewVideoBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
    }

    class ViewHolder constructor(private val binding: ViewVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Video, listener: VideoAdapterListener) {
            binding.apply {
                nameTextView.text = item.name
                val poster = item.backdropPath ?: item.posterPath
                val posterURL = URL_TMDB_IMAGE_W500 + poster
                Glide.with(posterImageView.context)
                    .load(posterURL)
                    .transform(CenterCrop())
                    .into(binding.posterImageView)
            }
            itemView.setOnClickListener {
                listener.onClick(item)
            }
        }
    }
}

class VideosAdapterDiffCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(
        oldItem: Video,
        newItem: Video
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Video,
        newItem: Video
    ): Boolean {
        return oldItem == newItem
    }
}
