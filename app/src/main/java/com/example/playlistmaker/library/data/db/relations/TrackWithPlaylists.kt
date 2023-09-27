package com.example.playlistmaker.library.data.db.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.data.db.entity.PlaylistTrackCrossRef
import com.example.playlistmaker.library.data.db.entity.SavedTrackEntity

data class TrackWithPlaylists(
    @Embedded val trackEntity: SavedTrackEntity,
    @Relation(
        parentColumn = "trackId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistTrackCrossRef::class)
    )
    val playlists: List<PlaylistEntity>
)
