package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {

    fun findTrack(request: String): Flow<Pair<ArrayList<Track>?, String?>>
    fun saveTrack(track: Track)
    fun showHistory(): List<Track>
    fun clearHistory()

}
