package com.example.playlistmaker.library.domain.db

import android.net.Uri
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    fun favoriteTracks(): Flow<List<Track>>

    suspend fun markAsFavorite(track: Track)

    suspend fun deleteFromFavorite(track: Track)

    suspend fun isFavorite(trackId: Long): Boolean

}