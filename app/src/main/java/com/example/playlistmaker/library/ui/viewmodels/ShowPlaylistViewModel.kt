package com.example.playlistmaker.library.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.ui.models.FavoriteState
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.launch

class ShowPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private lateinit var tracks: List<Track>
    private val _playlistLiveData = MutableLiveData<Playlist>()
    fun observeState(): LiveData<Playlist> = _playlistLiveData

    private val _trackListLiveData = MutableLiveData<List<Track>>()
    fun observeTrackListState(): LiveData<List<Track>> = _trackListLiveData

    private val _durationLiveData = MutableLiveData<Long>()
    fun observeDurationState(): LiveData<Long> = _durationLiveData


    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _playlistLiveData.postValue(playlistInteractor.getPlaylist(playlistId))
        }
    }

    fun getTracks(trackIdList: List<Long>) {
        Log.d("TRACKLISTDEBAG", "Работает метод getTracks() во вьюмодел")
        viewModelScope.launch {
            tracks = playlistInteractor.getTracks(trackIdList)
            _trackListLiveData.postValue(tracks)
        }
    }

    fun getDuration() {
        var duration: Long = 0
        tracks.forEach{
            duration += it.trackTimeMillis
        }
        _durationLiveData.postValue(duration)
    }

}