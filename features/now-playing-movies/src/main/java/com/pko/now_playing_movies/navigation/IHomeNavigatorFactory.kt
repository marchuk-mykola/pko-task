package com.pko.now_playing_movies.navigation

import androidx.navigation.NavController

interface IHomeNavigatorFactory {
    fun create(navController: NavController): IHomeNavigator
}

