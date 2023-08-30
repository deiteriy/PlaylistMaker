package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun loadPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

}