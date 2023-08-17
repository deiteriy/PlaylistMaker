package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun favoriteTracks(): Flow<List<Track>>

    suspend fun markAsFavorite(track: Track)

    suspend fun deleteFromFavorite(track: Track)
}