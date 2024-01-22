package com.pko.now_playing_movies.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pko.core.domain.models.Movie
import com.pko.core.domain.repositories.IMovieRepository
import com.pko.core.presentation.RecyclerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IMovieRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    private val currentState: HomeViewState
        get() = _uiState.value

    init {
        subscribeToFavourites()
        loadNowPlayingMovies()
    }

    private fun subscribeToFavourites() {
        viewModelScope.launch(dispatcher) {
            repository
                .favoriteMovieIdsAsFlow()
                .collect { movieIds ->
                    emit { copy(favorites = movieIds) }
                    updateFavoritesList()
                }
        }
    }

    private fun updateFavoritesList() {
        val movieIds = currentState.favorites
        val nowPlaying = currentState.nowPlayingViewState.nowPlaying
            .map { movieItem ->
                if (movieItem is RecyclerItem.MovieItem) {
                    movieItem.copy(
                        movie = movieItem.movie.copy(
                            favorite = movieIds.contains(movieItem.movie.id)
                        )
                    )
                } else {
                    movieItem
                }
            }

        val searchMovies = currentState.searchViewState.searchMovies
            .map { movieItem ->
                if (movieItem is RecyclerItem.MovieItem) {
                    movieItem.copy(
                        movie = movieItem.movie.copy(
                            favorite = movieIds.contains(movieItem.movie.id)
                        )
                    )
                } else {
                    movieItem
                }

            }

        emit {
            copy(
                nowPlayingViewState = nowPlayingViewState.copy(
                    nowPlaying = nowPlaying
                ),
                searchViewState = searchViewState.copy(
                    searchMovies = searchMovies
                )
            )
        }
    }

    fun loadMoreItemsIfNeeded(lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (shouldLoadMore(
                currentState.currentPage,
                currentState.totalPages,
                lastVisibleItemPosition,
                totalItemCount
            )
        ) {
            if (currentState.searchViewState.searchQuery.isEmpty()) {
                loadNowPlayingMovies()
            } else {
                searchMovies(currentState.searchViewState.searchQuery)
            }
        }
    }

    fun searchMovies(query: String) {
        val currentState = currentState.searchViewState
        if (query.isEmpty()) {
            // Reset search state if query is empty
            resetSearchState()
            return
        }

        // Load the first page for a new search query
        if (query != currentState.searchQuery) {
            emit {
                copy(
                    searchViewState = searchViewState.copy(
                        searchMovies = emptyList(),
                        searchQuery = query,
                        pagesLoaded = 0,
                        totalSearchPages = 1
                    )
                )
            }
            loadSearchPage(query, 1)
            return
        }

        // Check if more pages are available for the current search query
        if (currentState.pagesLoaded < currentState.totalSearchPages) {
            loadSearchPage(query, currentState.pagesLoaded + 1)
        }
    }

    private fun shouldLoadMore(
        currentPage: Int,
        totalPages: Int,
        lastVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        val isNearEndOfList = lastVisibleItemPosition >= totalItemCount - 3  // Threshold for loading more items
        val canLoadMore = currentPage < totalPages
        val isNotCurrentlyLoading = currentState.currentStateRecyclerItems.lastOrNull() !is RecyclerItem.Loading

        return isNearEndOfList && canLoadMore && isNotCurrentlyLoading
    }

    private fun loadNowPlayingMovies() {
        if (shouldLoadNowPlaying()) {
            setNowPlayingLoadingState()
            fetchNowPlayingMovies()
        }
    }

    private fun shouldLoadNowPlaying(): Boolean {
        val nowPlayingState = currentState.nowPlayingViewState
        return nowPlayingState.nowPlaying.lastOrNull() !is RecyclerItem.Loading &&
              nowPlayingState.pagesLoaded <= nowPlayingState.totalNowPlayingPages
    }

    private fun setNowPlayingLoadingState() {
        emitNowPlayingState(RecyclerItem.Loading)
    }

    private fun fetchNowPlayingMovies() {
        val nowPlayingState = currentState.nowPlayingViewState

        viewModelScope.launch(dispatcher) {
            repository
                .getNowPlayingMovies(nowPlayingState.pagesLoaded + 1)
                .onSuccess { movieResponse ->
                    if (movieResponse.results.isEmpty()) {
                        emit {
                            copy(
                                nowPlayingViewState = nowPlayingViewState.copy(
                                    nowPlaying = listOf(RecyclerItem.EmptyResult)
                                )
                            )
                        }
                    } else {
                        val newMovies = movieResponse.results.map { RecyclerItem.MovieItem(it) }

                        val updatedList =
                            nowPlayingState.nowPlaying.filterIsInstance<RecyclerItem.MovieItem>() + newMovies
                        emit {
                            copy(
                                nowPlayingViewState = nowPlayingViewState.copy(
                                    nowPlaying = updatedList,
                                    pagesLoaded = movieResponse.page,
                                    totalNowPlayingPages = movieResponse.totalPages
                                )
                            )
                        }

                        updateFavoritesList()
                    }
                }
                .onFailure { exception ->
                    emit {
                        copy(
                            nowPlayingViewState = nowPlayingViewState.copy(
                                nowPlaying = listOf(RecyclerItem.Error),
                                pagesLoaded = 0,
                                totalNowPlayingPages = 1
                            )
                        )
                    }
                }
        }
    }


    private fun loadSearchPage(query: String, page: Int) {
        emit {
            copy(
                searchViewState = searchViewState.copy(
                    searchMovies = searchViewState.searchMovies + RecyclerItem.Loading,
                    searchQuery = query,
                )
            )
        }

        viewModelScope.launch(dispatcher) {
            repository
                .searchMovies(query, page)
                .onSuccess { searchResponse ->
                    if (searchResponse.results.isEmpty()) {
                        emit {
                            copy(
                                searchViewState = searchViewState.copy(
                                    searchMovies = listOf(RecyclerItem.EmptyResult),
                                    searchQuery = query
                                )
                            )
                        }
                    } else {
                        val newSearchResults = searchResponse.results.map { RecyclerItem.MovieItem(it) }

                        val updatedList = currentState.searchViewState.searchMovies
                            .filterIsInstance<RecyclerItem.MovieItem>() + newSearchResults

                        emit {
                            copy(
                                searchViewState = searchViewState.copy(
                                    searchMovies = updatedList,
                                    searchQuery = query,
                                    pagesLoaded = searchResponse.page,
                                    totalSearchPages = searchResponse.totalPages
                                )
                            )
                        }

                        updateFavoritesList()
                    }
                }
                .onFailure { exception ->
                    emit {
                        copy(
                            searchViewState = searchViewState.copy(
                                searchMovies = listOf(RecyclerItem.Error),
                                searchQuery = query,
                                pagesLoaded = 0,
                                totalSearchPages = 1
                            )
                        )
                    }
                }
        }
    }

    private fun resetSearchState() {
        emit {
            copy(searchViewState = SearchViewState())
        }
    }

    private fun emitSearchState(item: RecyclerItem) {
        val searchState = currentState.searchViewState
        _uiState.value = currentState.copy(
            searchViewState = searchState.copy(
                searchMovies = searchState.searchMovies + item
            )
        )
    }

    private fun emitNowPlayingState(item: RecyclerItem) {
        _uiState.value = currentState.copy(
            nowPlayingViewState = currentState.nowPlayingViewState.copy(
                nowPlaying = currentState.nowPlayingViewState.nowPlaying + item
            )
        )
    }

    private fun emit(reducer: HomeViewState.() -> HomeViewState) {
        _uiState.value = currentState.reducer()
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch(dispatcher) {
            if (movie.favorite) {
                repository.removeFromFavorites(movie.id)
            } else {
                repository.addToFavorites(movie.id)
            }
        }
    }

}

