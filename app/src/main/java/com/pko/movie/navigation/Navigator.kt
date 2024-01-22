package com.pko.movie.navigation

import androidx.navigation.NavController
import com.pko.now_playing_movies.navigation.IHomeNavigator
import com.pko.now_playing_movies.ui.HomeFragmentDirections

class Navigator(
    private val navController: NavController
) : IHomeNavigator {

    override fun navigateToMovieDetails(movieId: Int) {
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(movieId))
    }
}


