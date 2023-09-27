package com.example.playlistmaker.search.data.dto

data class TrackDto (
    val trackName: String,      // Название композиции
    val artistName: String,     // Имя исполнителя
    val trackTimeMillis: Long,     // Продолжительность трека
    val artworkUrl100: String?,    // ссылка на мини-обложку
    val artworkUrl60: String?,
    val trackId: Long, // Ссылка на изображение обложки
    val collectionName: String?,    // название альбома
    val releaseDate: String?, // Год выпуска
    val primaryGenreName: String, // Жанр
    val country: String,  // страна выпуска трека
    val previewUrl: String?, // ссылка на отрывок песни в 30 секунд
)