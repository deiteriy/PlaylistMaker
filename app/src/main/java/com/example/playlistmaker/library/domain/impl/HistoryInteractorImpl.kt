package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.HistoryInteractor
import com.example.playlistmaker.library.domain.db.HistoryRepository
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl(private val historyRepository: HistoryRepository): HistoryInteractor {
    override fun historyTracks(): Flow<List<Track>> {
        return historyRepository.favoriteTracks()
    }
}