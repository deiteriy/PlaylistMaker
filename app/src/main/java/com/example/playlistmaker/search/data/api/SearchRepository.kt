package com.example.playlistmaker.search.data.api

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun findTrack(request: String): Flow<Resource<ArrayList<Track>>>
    fun saveTrack(track: Track)
    fun showHistory(): List<Track>
    fun clearHistory()
}
