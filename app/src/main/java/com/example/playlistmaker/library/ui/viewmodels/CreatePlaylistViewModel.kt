package com.example.playlistmaker.library.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private var currentImage: Uri? = null

    fun createPlaylist(title: String, description: String?) {
        viewModelScope.launch {
            val playlist = Playlist(name = title, description = description, playlistCover = currentImage, tracks = arrayListOf(), tracksCount = 0)
            playlistInteractor.addPlaylist(playlist)
        }
    }

    fun saveImage(uri: Uri) {
        currentImage = playlistInteractor.saveImageAndReturnUri(uri)
    }


}