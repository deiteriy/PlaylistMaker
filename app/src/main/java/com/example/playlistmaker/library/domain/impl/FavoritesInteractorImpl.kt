package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.domain.db.FavoritesRepository
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository): FavoritesInteractor {
    override fun favoriteTracks(): Flow<List<Track>> {
        return favoritesRepository.favoriteTracks()
    }

    override suspend fun markAsFavorite(track: Track) {
        favoritesRepository.markAsFavorite(track)
    }

    override suspend fun deleteFromFavorite(track: Track) {
        favoritesRepository.deleteFromFavorite(track)
    }

    override suspend fun isFavorite(trackId: Long): Boolean {
        return favoritesRepository.isFavorite(trackId)
    }

}