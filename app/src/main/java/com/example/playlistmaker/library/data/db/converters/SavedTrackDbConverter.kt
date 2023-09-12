package com.example.playlistmaker.library.data.db.converters

import com.example.playlistmaker.library.data.db.entity.SavedTrackEntity
import com.example.playlistmaker.player.domain.models.Track

class SavedTrackDbConverter {
    fun map(track: Track): SavedTrackEntity {
        return SavedTrackEntity(track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl, System.currentTimeMillis())
    }

    fun map(track: SavedTrackEntity): Track {
        return Track(track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl)
    }
}