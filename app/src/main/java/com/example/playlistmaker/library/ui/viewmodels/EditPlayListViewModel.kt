package com.example.playlistmaker.library.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch

class EditPlayListViewModel(private val playlistInteractor: PlaylistInteractor) : CreatePlaylistViewModel(playlistInteractor) {

    val _playlistLiveData = MutableLiveData<Playlist>()
    lateinit var oldImage: Uri
    fun observePlaylist(): LiveData<Playlist> = _playlistLiveData

    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylist(playlistId)
            if(playlist.playlistCover != null) {
                oldImage = playlist.playlistCover!!
            }
            _playlistLiveData.postValue(playlist)
        }
    }

    fun saveEditPlaylist(playlist: Playlist) {
        if(currentImage != null) {
            playlist.playlistCover = currentImage
            if(oldImage != currentImage) {
                playlistInteractor.deleteImage(oldImage)
            }
        }
        viewModelScope.launch {
            playlistInteractor.addPlaylist(playlist)
        }
    }

}