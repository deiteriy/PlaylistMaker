package com.example.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("saved_track_table")
class SavedTrackEntity(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    @PrimaryKey
    val trackId: Long,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
    val timeStamp: Long,
) {
}