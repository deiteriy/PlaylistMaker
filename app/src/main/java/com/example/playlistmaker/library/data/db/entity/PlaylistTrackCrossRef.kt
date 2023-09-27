package com.example.playlistmaker.library.data.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackCrossRef(
  val playlistId: Long,
  val trackId: Long
)