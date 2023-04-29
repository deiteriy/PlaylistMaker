package com.example.playlistmaker
import java.io.Serializable

data class Track (
    val trackName: String,      // Название композиции
    val artistName: String,     // Имя исполнителя
    val trackTimeMillis: Long,     // Продолжительность трека
    val artworkUrl100: String,
    val trackId: Long, // Ссылка на изображение обложки
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    ): Serializable {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}
