package com.example.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    val trackName: String,      // Название композиции
    val artistName: String,     // Имя исполнителя
    val trackTimeMillis: Long,     // Продолжительность трека
    val artworkUrl100: String?,    // ссылка на мини-обложку
    @PrimaryKey
    val trackId: Long,
    val collectionName: String?,    // название альбома
    val releaseDate: String?, // Год выпуска
    val primaryGenreName: String, // Жанр
    val country: String,  // страна выпуска трека
    val previewUrl: String, // ссылка на отрывок песни в 30 секунд
) {
}