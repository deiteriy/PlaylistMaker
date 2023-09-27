package com.example.playlistmaker.library.domain.models

import android.net.Uri


data class Playlist(
    val playlistId: Long = 0,
    var name: String,
    var description: String?,
    var playlistCover: Uri? = null,
    var tracks: ArrayList<Long>,
    var tracksCount: Long = tracks.size.toLong(),
)