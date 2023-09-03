package com.example.playlistmaker.library.domain.models

import android.net.Uri


class Playlist(
    val playlistId: Long = 0,
    val name: String,
    val description: String?,
    val playlistCover: Uri?,
    var tracks: ArrayList<Long>,
    var tracksCount: Long,
)