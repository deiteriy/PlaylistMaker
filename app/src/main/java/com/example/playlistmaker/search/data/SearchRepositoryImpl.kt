package com.example.playlistmaker.search.data

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.SearchHistory
import com.example.playlistmaker.search.data.api.SearchRepository
import com.example.playlistmaker.search.domain.NetworkError

class SearchRepositoryImpl(private val networkClient: NetworkClient, private val searchHistory: SearchHistory):
    SearchRepository {
    override fun findTrack(
        request: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        networkClient.doRequest(request, onSuccess, onError)

    }

    override fun saveTrack(track: Track) {
        searchHistory.write(track)
    }


    override fun showHistory(): List<Track> {
        return searchHistory.read()
    }

    override fun clearHistory() {
        searchHistory.clear()
    }
}