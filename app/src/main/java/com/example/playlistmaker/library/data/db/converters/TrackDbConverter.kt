package com.example.playlistmaker.library.data.db.converters

import com.example.playlistmaker.library.data.db.entity.TrackEntity
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.data.dto.TrackDto

class TrackDbConverter {

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl)
    }

    fun map(track: TrackEntity): Track {
        return Track(track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.trackId, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl)
    }
}