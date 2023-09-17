package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.data.db.dao.PlaylistTrackCrossRefDao
import com.example.playlistmaker.library.data.db.dao.SavedTrackDao
import com.example.playlistmaker.library.data.db.dao.TrackDao
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.data.db.entity.PlaylistTrackCrossRef
import com.example.playlistmaker.library.data.db.entity.SavedTrackEntity
import com.example.playlistmaker.library.data.db.entity.TrackEntity


@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class, SavedTrackEntity::class, PlaylistTrackCrossRef::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun savedTrackDao(): SavedTrackDao

    abstract fun playlistTrackCrossRefDao(): PlaylistTrackCrossRefDao
}