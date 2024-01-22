package com.pko.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pko.core.domain.models.MovieDetailResponse
import com.pko.core.presentation.BuildConfig
import com.pko.core.presentation.calculateRating
import com.pko.core.presentation.getFormattedMovieDescription
import com.pko.core.presentation.getFormattedRating
import com.pko.details.viewModel.DetailState
import com.pko.details.viewModel.DetailViewModel
import com.pko.details.R
import com.pko.details.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import gone
import kotlinx.coroutines.launch
import showToast
import visible

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMovieDetailBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt("movieId")?.let(viewModel::getMovieDetails)
        observeMovie()
    }

    private fun observeMovie() = viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiState.collect { state ->
                when (state) {
                    is DetailState.Loading -> {
                        showLoading()
                    }
                    is DetailState.Success -> {
                        setupUi(state.movieDetails)
                    }
                    is DetailState.Error -> {
                        showError()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBarDetail.visible()
        binding.tvMovieDetails.gone()
        binding.tvError.gone()
    }

    private fun showError() {
        binding.progressBarDetail.gone()
        binding.tvMovieDetails.gone()
        binding.tvError.visible()
    }

    private fun setupUi(movieResponse: MovieDetailResponse) = with(binding) {
        binding.tvMovieDetails.visible()
        binding.progressBarDetail.gone()
        binding.tvError.gone()

        tvMovieTitle.text = movieResponse.title
        Glide.with(this@MovieDetailFragment)
            .load("${BuildConfig.imageBaseUrl}${movieResponse.posterPath}")
            .into(imgBackground)

        tvSubtitle.text = movieResponse.getFormattedMovieDescription()
        movieRating.apply {
            numStars = 5
            stepSize = 0.5f
            rating = movieResponse.calculateRating()
        }

        movieRatingText.text = movieResponse.getFormattedRating()
        tvDescription.text = movieResponse.overview

        ivFavourite.apply {
            setImageResource(
                if (movieResponse.favorite)
                    com.pko.core.presentation.R.drawable.ic_favorite
                else
                    com.pko.core.presentation.R.drawable.ic_favorite_border
            )
            setOnClickListener { viewModel.onHeartClicked(movieResponse.id) }
        }

        ivBack.setOnClickListener { findNavController().navigateUp() }

        recyclerviewGenres.adapter = GenreAdapter(movieResponse.genres.orEmpty())
        recyclerviewGenres.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            requireContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}