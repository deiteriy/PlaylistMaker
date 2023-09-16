package com.example.playlistmaker.library.domain.models

import android.net.Uri
import android.os.Parcelable
import java.io.Serializable


class Playlist(
    val playlistId: Long = 0,
    var name: String,
    var description: String?,
    var playlistCover: Uri?,
    var tracks: ArrayList<Long>,
    var tracksCount: Long = tracks.size.toLong(),
)