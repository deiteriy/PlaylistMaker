package com.example.playlistmaker.search.data

import android.util.Log
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackMapper(private val appDatabase: AppDatabase) {

    fun mapToDomain(trackDto: TrackDto): Track {
        return Track(
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis,
            artworkUrl100 = trackDto.artworkUrl100,
            trackId = trackDto.trackId,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl,
        )
    }

    fun mapToDto(track: Track): TrackDto {
        return TrackDto(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
    }
}