package com.pko.movie.navigation

import androidx.navigation.NavController
import com.pko.now_playing_movies.navigation.IHomeNavigator
import com.pko.now_playing_movies.navigation.IHomeNavigatorFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigatorModule {

    @Binds
    abstract fun bindHomeNavigatorFactory(impl: HomeNavigatorFactoryImpl): IHomeNavigatorFactory
}

class HomeNavigatorFactoryImpl @Inject constructor() : IHomeNavigatorFactory {
    override fun create(navController: NavController): IHomeNavigator {
        return Navigator(navController)
    }
}