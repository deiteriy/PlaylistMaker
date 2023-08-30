package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {

    override fun loadPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.loadPlaylists()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }
}