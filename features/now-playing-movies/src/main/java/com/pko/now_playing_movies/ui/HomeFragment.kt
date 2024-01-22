package com.pko.now_playing_movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pko.now_playing_movies.R
import com.pko.now_playing_movies.viewModel.HomeViewModel
import com.pko.now_playing_movies.databinding.FragmentHomeBinding
import com.pko.now_playing_movies.navigation.IHomeNavigator
import com.pko.now_playing_movies.navigation.IHomeNavigatorFactory
import com.pko.now_playing_movies.viewModel.HomeViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigatorFactory: IHomeNavigatorFactory

    private lateinit var navigator: IHomeNavigator

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var searchTitlesAdapter: ArrayAdapter<String>

    private val searchQueryFlow = MutableStateFlow("")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = navigatorFactory.create(findNavController())
        setUpRecyclerView()
        setupSearchView()
        collectUiState()
        setupSearchQueryFlow()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleCurrentState(state)
                }
            }
        }
    }

    private fun handleCurrentState(
        state: HomeViewState
    ) {
        binding.tvListTitle.text = if (state.searchViewState.searchQuery.isEmpty()) {
            getString(R.string.now_playing)
        } else {
            getString(R.string.search_results)
        }

        movieAdapter.submitList(state.currentStateRecyclerItems)
        searchTitlesAdapter.clear()
        searchTitlesAdapter.addAll(state.suggestions)

        if (state.currentPage == 1 && searchQueryFlow.value.isEmpty()) {
            binding.rvMovies.scrollToPosition(0)
        }
    }

    private fun setupSearchQueryFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            searchQueryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    viewModel.searchMovies(query)
                }
        }
    }

    private fun setUpRecyclerView() {
        movieAdapter = MovieAdapter(
            onItemClickListener = {
                navigator.navigateToMovieDetails(it.id)
            },
            onHeartClickListener = {
                viewModel.toggleFavorite(it)
            }
        )
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy <= 0) return // Only load more when scrolling down

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    viewModel.loadMoreItemsIfNeeded(lastVisibleItemPosition, totalItemCount)
                }
            })
        }
    }

    private fun setupSearchView() {
        searchTitlesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)

        binding.searchView.apply {
            setAdapter(searchTitlesAdapter)
            threshold = 1
            setOnItemClickListener { _, _, position, _ ->
                val selectedTitle = searchTitlesAdapter.getItem(position)
                setText(selectedTitle)
            }
            addTextChangedListener { editable ->
                val query = editable?.toString() ?: ""
                viewLifecycleOwner.lifecycleScope.launch {
                    searchQueryFlow.emit(query)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
