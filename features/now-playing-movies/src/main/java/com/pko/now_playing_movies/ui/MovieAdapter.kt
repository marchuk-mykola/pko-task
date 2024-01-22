package com.pko.now_playing_movies.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pko.core.domain.models.Movie
import com.pko.core.presentation.BuildConfig
import com.pko.core.presentation.RecyclerItem
import com.pko.now_playing_movies.R
import com.pko.now_playing_movies.databinding.MovieListBinding

class MovieAdapter(
    private val onItemClickListener: (Movie) -> Unit,
    private val onHeartClickListener: (Movie) -> Unit
) : ListAdapter<RecyclerItem, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_LOADING = 1
        private const val TYPE_ERROR = 2
        private const val TYPE_EMPTY = 3
    }

    inner class MovieViewHolder(val binding: MovieListBinding) : RecyclerView.ViewHolder(binding.root)
    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)
    class ErrorViewHolder(view: View) : RecyclerView.ViewHolder(view)
    class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private class MovieDiffCallback : DiffUtil.ItemCallback<RecyclerItem>() {
        override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            return (oldItem is RecyclerItem.MovieItem && newItem is RecyclerItem.MovieItem &&
                  oldItem.movie.id == newItem.movie.id)
        }

        override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecyclerItem.MovieItem -> TYPE_MOVIE
            is RecyclerItem.Loading -> TYPE_LOADING
            is RecyclerItem.Error -> TYPE_ERROR
            is RecyclerItem.EmptyResult -> TYPE_EMPTY
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MOVIE -> {
                val binding = MovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieViewHolder(binding)
            }

            TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(view)
            }

            TYPE_ERROR -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_error, parent, false)
                ErrorViewHolder(view)
            }

            TYPE_EMPTY -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
                EmptyViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is RecyclerItem.MovieItem -> (holder as MovieViewHolder).bind(item.movie)
            is RecyclerItem.Loading -> Unit // Nothing to bind for loading
            is RecyclerItem.Error -> Unit // Nothing to bind for error
            is RecyclerItem.EmptyResult -> Unit // Nothing to bind for EmptyResult
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    private fun MovieViewHolder.bind(movie: Movie) {
        binding.apply {
            Glide
                .with(this.root.context)
                .load(BuildConfig.imageBaseUrl + movie.posterPath)
                .into(moviePoster)
            movieTitle.text = movie.title
            movieDescription.text = movie.overview
            movieRating.numStars = 5
            movieRating.stepSize = 0.5f
            movieRating.rating = movie.rating
            movieRatingText.text = movie.formattedRating
            root.setOnClickListener {
                onItemClickListener(movie)
            }
            ivFavourite.apply {
                setImageResource(
                    if (movie.favorite)
                        com.pko.core.presentation.R.drawable.ic_favorite
                    else
                        com.pko.core.presentation.R.drawable.ic_favorite_border
                )
                setOnClickListener {
                    onHeartClickListener(movie)
                }
            }
        }
    }

}
