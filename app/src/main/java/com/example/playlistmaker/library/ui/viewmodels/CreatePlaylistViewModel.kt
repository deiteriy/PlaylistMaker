package com.example.playlistmaker.library.ui.viewmodels

import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch
import java.io.File

class CreatePlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun createPlaylist(title: String, description: String?, cover:Uri?) {
        viewModelScope.launch {
            val playlist = Playlist(name = title, description = description, playlistCover = cover, tracks = arrayListOf(), tracksCount = 0)
            playlistInteractor.addPlaylist(playlist)
        }
    }


}