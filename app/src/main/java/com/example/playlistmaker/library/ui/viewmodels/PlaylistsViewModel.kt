package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.ui.models.PlaylistState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _playlistLiveData = MutableLiveData<PlaylistState>()

    fun observeState(): LiveData<PlaylistState> = _playlistLiveData

    fun showPlaylists() {
        viewModelScope.launch {
            playlistInteractor.loadPlaylists().collect {
                if(it.isEmpty()) {
                    _playlistLiveData.postValue(PlaylistState.PlaylistEmptyState())
                } else {
                    _playlistLiveData.postValue(PlaylistState.PlaylistFullState(it))
                }
            }
        }
    }


}