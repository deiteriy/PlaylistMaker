package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entity.PlaylistTrackCrossRef

@Dao
interface PlaylistTrackCrossRefDao {

    @Insert
    suspend fun insertCrossRef(crossRef: PlaylistTrackCrossRef)

    @Delete
    suspend fun deleteCrossRef(crossRef: PlaylistTrackCrossRef)

    @Query("SELECT * FROM PlaylistTrackCrossRef WHERE trackId = :trackId")
    suspend fun getPlaylistsContainingTrack(trackId: Long): List<PlaylistTrackCrossRef>
}