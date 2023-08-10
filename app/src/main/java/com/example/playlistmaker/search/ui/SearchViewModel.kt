package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.ui.model.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val interactor: SearchInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    private val historyList = ArrayList<Track>()
    private var searchJob: Job? = null
    private var isClickAllowed = true
    private var searchRequest = SEARCH_VALUE

    init {
        historyList.addAll(interactor.showHistory())
        stateLiveData.postValue(SearchState.SearchHistory(historyList))
    }

    fun observeState(): LiveData<SearchState> = stateLiveData
    fun findTrack(request: String) {
        stateLiveData.postValue(SearchState.Loading)
        searchRequest = request

        interactor.findTrack(request,
            onSuccess = { trackList ->
                stateLiveData.postValue(SearchState.SearchedTracks(trackList))
            },
            onError = { error ->
                stateLiveData.postValue(SearchState.SearchError(error))
            }
        )

    }

    fun clearHistory()  {
        interactor.clearHistory()
        stateLiveData.postValue(SearchState.SearchHistory(interactor.showHistory()))
    }

    fun saveTrack(track: Track) {
        interactor.saveTrack(track)
    }

    fun showHistory(): List<Track> {
        stateLiveData.postValue(SearchState.SearchHistory(interactor.showHistory()))
        return interactor.showHistory()
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun searchDebounce(request: String) {
        if (searchRequest == request) {
            return
        }
        searchRequest = request
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            if (searchRequest.isNotEmpty()) {
                findTrack(searchRequest)
            }
        }
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}