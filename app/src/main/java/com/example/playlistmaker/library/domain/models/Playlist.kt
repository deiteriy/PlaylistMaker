package com.example.playlistmaker.library.domain.models

import android.net.Uri
import java.io.Serializable


class Playlist(
    val playlistId: Long = 0,
    val name: String,
    val description: String?,
    val playlistCover: Uri?,
    var tracks: ArrayList<Long>,
    var tracksCount: Long = tracks.size.toLong(),
): Serializable