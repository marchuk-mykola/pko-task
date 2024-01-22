package com.pko.details.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pko.core.domain.models.MovieGenre
import com.pko.details.databinding.GenreItemBinding

class GenreAdapter(
    private var genreList: List<MovieGenre>
) : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    class GenreViewHolder(val binding: GenreItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = GenreItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return genreList.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.binding.tvGenre.text = genreList[position].name.toString()
    }

}