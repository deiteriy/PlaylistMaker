package com.example.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long,
    val name: String,
    val description: String?,
    val playlistCover: String?,
    var tracks: String,
    var tracksCount: Long = 0,
    var timeStamp: Long,
    )
