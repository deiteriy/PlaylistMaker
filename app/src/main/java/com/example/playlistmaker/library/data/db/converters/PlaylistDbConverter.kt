package com.example.playlistmaker.library.data.db.converters

import android.net.Uri
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConverter(private val gson: Gson) {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(playlist.playlistId, playlist.name, playlist.description, playlist.playlistCover.toString(), tracksToString(playlist.tracks), playlist.tracksCount, System.currentTimeMillis())
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(playlist.playlistId, playlist.name, playlist.description, Uri.parse(playlist.playlistCover), tracksToList(playlist.tracks), playlist.tracksCount)
    }

    private fun tracksToString(tracksId: List<Long>): String {
        return gson.toJson(tracksId)
    }

    private fun tracksToList(jsonString: String): List<Long> {
        val trackListType = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(jsonString, trackListType)
    }
}