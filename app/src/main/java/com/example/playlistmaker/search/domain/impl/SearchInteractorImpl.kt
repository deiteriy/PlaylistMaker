package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.data.api.SearchRepository
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: SearchRepository): SearchInteractor {
    override fun findTrack(request: String): Flow<Pair<ArrayList<Track>?, String?>> {
        return repository.findTrack(request).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }


    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override suspend fun showHistory(): List<Track> {
        return repository.showHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}