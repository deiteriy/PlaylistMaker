package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.domain.NetworkError
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.SearchRepository

class SearchInteractorImpl(private val repository: SearchRepository): SearchInteractor {
    override fun findTrack(
        request: String,
        onSuccess: (List<Track>) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        repository.findTrack(request, onSuccess, onError)
    }


    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override fun showHistory(): List<Track> {
        return repository.showHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}