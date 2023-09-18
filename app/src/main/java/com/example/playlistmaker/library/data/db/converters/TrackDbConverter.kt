package com.example.playlistmaker.library.data.db.converters

import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.player.domain.models.Track

class TrackDbConverter {

    fun map(track: Track): TrackEntity {
        return TrackEntity(track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.artworkUrl60, track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl, System.currentTimeMillis())
    }

    fun map(track: TrackEntity): Track {
        return Track(track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.artworkUrl60, track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl, isFavorite = true)
    }
}