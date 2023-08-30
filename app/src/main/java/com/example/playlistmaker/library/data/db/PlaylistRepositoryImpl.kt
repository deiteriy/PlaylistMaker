package com.example.playlistmaker.library.data.db

import com.example.playlistmaker.library.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistRepository {
    override fun loadPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(convertFromPlaylist(playlist))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }
    private fun convertFromPlaylist(playlist: Playlist): PlaylistEntity {
        return playlistDbConverter.map(playlist)
    }
}