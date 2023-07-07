package com.example.playlistmaker.search.ui.model

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.domain.NetworkError

sealed class SearchState {
    class SearchHistory(val tracks: List<Track>): SearchState()
    class SearchedTracks(val tracks: List<Track>): SearchState()
    class SearchError(val error: NetworkError) : SearchState()
    object Loading : SearchState()
}
