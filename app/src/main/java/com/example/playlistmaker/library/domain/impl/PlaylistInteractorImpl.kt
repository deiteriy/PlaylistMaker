package com.example.playlistmaker.library.domain.impl

import android.net.Uri
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow


class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {

    override fun loadPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.loadPlaylists()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistRepository.addPlaylist(playlist)
    }

    override suspend fun saveTrack(playlist: Playlist, track: Track) {
        playlistRepository.saveTrack(playlist, track)
    }

    override suspend fun getPlaylist(playlistId: Long): Playlist {
        return playlistRepository.getPlaylist(playlistId)
    }

    override suspend fun getTracks(trackIdList: List<Long>): List<Track> {
        return playlistRepository.getTracks(trackIdList)
    }

    override fun saveImageAndReturnUri(uri: Uri): Uri {
        return playlistRepository.saveImageAndReturnUri(uri)
    }

    override suspend fun deleteTrack(trackId: Long, playlist: Playlist) {
        playlistRepository.deleteTrack(trackId, playlist)
    }
}