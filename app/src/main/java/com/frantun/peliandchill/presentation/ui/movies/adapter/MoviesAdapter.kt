package com.frantun.peliandchill.presentation.ui.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frantun.peliandchill.common.Constants.URL_TMDB_IMAGE_W500
import com.frantun.peliandchill.databinding.ViewMovieBinding
import com.frantun.peliandchill.domain.model.Movie

class MoviesAdapter : ListAdapter<Movie, MoviesAdapter.ViewHolder>(MoviesAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewMovieBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder constructor(private val binding: ViewMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {
            binding.apply {
                titleTextView.text = item.title
                val posterURL = URL_TMDB_IMAGE_W500 + item.posterPath
                Glide.with(posterImageView.context)
                    .load(posterURL)
                    .into(binding.posterImageView)
            }
        }
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