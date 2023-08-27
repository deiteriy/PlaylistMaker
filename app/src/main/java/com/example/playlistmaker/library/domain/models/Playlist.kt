package com.example.playlistmaker.library.domain.models

import android.net.Uri


class Playlist(
    val playlistId: Long,
    val name: String,
    val description: String?,
    val playlistCover: Uri,
    var tracks: List<Long>,
    var tracksCount: Long,
    var timeStamp: Long,
)