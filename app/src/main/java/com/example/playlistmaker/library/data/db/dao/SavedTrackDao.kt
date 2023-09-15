package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.data.db.entity.SavedTrackEntity
import com.example.playlistmaker.library.data.db.entity.TrackEntity

@Dao
interface SavedTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: SavedTrackEntity)

    @Query("SELECT * FROM saved_track_table")
    suspend fun getTracks(): List<SavedTrackEntity>
}