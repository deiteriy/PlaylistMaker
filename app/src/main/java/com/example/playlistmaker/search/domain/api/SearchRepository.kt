package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.domain.NetworkError

interface SearchRepository {
    fun findTrack(request: String, onSuccess: (List<Track>) -> Unit, onError: (NetworkError) -> Unit)
    fun saveTrack(track: Track)
    fun showHistory(): List<Track>
    fun clearHistory()
}
