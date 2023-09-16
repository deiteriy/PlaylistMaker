package com.example.playlistmaker.library.data.db

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.library.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.library.data.db.converters.SavedTrackDbConverter
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.data.db.entity.SavedTrackEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val savedTrackDbConverter: SavedTrackDbConverter,
    private val context: Context,
) : PlaylistRepository {
    override fun loadPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromListPlaylistEntity(playlists))
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(convertFromPlaylist(playlist))
    }

    override suspend fun saveTrack(playlist: Playlist, track: Track) {
        playlist.tracks.add(track.trackId)
        addPlaylist(playlist)
        appDatabase.savedTrackDao().insertTrack(convertFromTrack(track))
    }

    override suspend fun getPlaylist(playlistId: Long): Playlist {
        return convertFromPlaylistEntity(appDatabase.playlistDao().getPlaylist(playlistId = playlistId))
    }

    override suspend fun getTracks(trackIdList: List<Long>): List<Track> {
        val savedTrackEntityList = appDatabase.savedTrackDao().getTracks()
        val trackList = arrayListOf<Track>()
        trackIdList.forEach {
            val trackId = it
            val trackEntity = savedTrackEntityList.find { it.trackId == trackId }
            trackEntity?.let {
                trackList.add(convertFromTrackEntity(trackEntity))
            }
        }

        return trackList
    }

    override suspend fun deleteTrack(trackId: Long, playlist: Playlist) {
        playlist.tracks.remove(trackId)
        addPlaylist(playlist)

    }

    override suspend fun deletePlaylist(playlistId: Long) {
        appDatabase.playlistDao().deletePlaylist(playlistId)
    }

    override fun saveImageAndReturnUri(uri: Uri): Uri {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlistcoveralbum")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val currentTimeMillis = System.currentTimeMillis()
        val fileName = "image_${currentTimeMillis}.jpg"

        val file = File(filePath, fileName)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
    }

    private fun convertFromListPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }
    private fun convertFromPlaylist(playlist: Playlist): PlaylistEntity {
        return playlistDbConverter.map(playlist)
    }

    private fun convertFromPlaylistEntity(playlistEntity: PlaylistEntity): Playlist {
        return playlistDbConverter.map(playlistEntity)
    }

    private fun convertFromTrack(track: Track): SavedTrackEntity {
        return savedTrackDbConverter.map(track)
    }

    private fun convertFromTrackEntity(trackEntity: SavedTrackEntity): Track {
        return savedTrackDbConverter.map(trackEntity)
    }
}