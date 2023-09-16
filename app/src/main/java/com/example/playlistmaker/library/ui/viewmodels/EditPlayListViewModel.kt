package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch
import java.io.FileDescriptor

class EditPlayListViewModel(private val playlistInteractor: PlaylistInteractor) : CreatePlaylistViewModel(playlistInteractor) {

    val _playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = _playlistLiveData

    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _playlistLiveData.postValue(playlistInteractor.getPlaylist(playlistId))
        }
    }

    fun saveEditPlaylist(playlist: Playlist) {
        if(currentImage != null) {
            playlist.playlistCover = currentImage
        }
        viewModelScope.launch {
            playlistInteractor.addPlaylist(playlist)
        }
    }

}