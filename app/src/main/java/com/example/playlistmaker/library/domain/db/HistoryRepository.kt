package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun favoriteTracks(): Flow<List<Track>>
}