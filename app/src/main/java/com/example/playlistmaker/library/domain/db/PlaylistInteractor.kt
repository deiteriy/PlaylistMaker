package com.example.playlistmaker.library.domain.db

import android.net.Uri
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun loadPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun saveTrack(playlist: Playlist, track: Track)

    fun saveImageAndReturnUri(uri: Uri): Uri

}