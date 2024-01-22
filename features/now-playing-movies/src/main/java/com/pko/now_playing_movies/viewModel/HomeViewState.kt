package com.pko.now_playing_movies.viewModel

import com.pko.core.presentation.RecyclerItem

data class HomeViewState(
    val searchViewState: SearchViewState = SearchViewState(),
    val nowPlayingViewState: NowPlayingViewState = NowPlayingViewState(),
    val favorites: List<Int> = emptyList()
) {

    val currentStateRecyclerItems: List<RecyclerItem>
        get() = if (searchViewState.searchQuery.isEmpty()) {
            nowPlayingViewState.nowPlaying
        } else {
            searchViewState.searchMovies
        }

    val suggestions: List<String>
        get() = searchViewState.searchSuggestions.distinct()

    val currentPage
        get() = if (searchViewState.searchQuery.isEmpty())
            nowPlayingViewState.pagesLoaded else searchViewState.pagesLoaded

    val totalPages
        get() = if (searchViewState.searchQuery.isEmpty())
            nowPlayingViewState.totalNowPlayingPages else searchViewState.totalSearchPages

}

data class SearchViewState(
    val searchMovies: List<RecyclerItem> = emptyList(),
    val searchQuery: String = "",
    val pagesLoaded: Int = 0,
    val totalSearchPages: Int = 1
) {
    val searchSuggestions: List<String>
        get() = searchMovies.filterIsInstance<RecyclerItem.MovieItem>().map { it.movie.title }
}

data class NowPlayingViewState(
    val nowPlaying: List<RecyclerItem> = emptyList(),
    val pagesLoaded: Int = 0,
    val totalNowPlayingPages: Int = 1
)