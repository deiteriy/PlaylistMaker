package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShowPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private lateinit var tracks: List<Track>
    private val _playlistLiveData = MutableLiveData<Playlist>()
    fun observeState(): LiveData<Playlist> = _playlistLiveData

    private val _trackListLiveData = MutableLiveData<List<Track>>()
    fun observeTrackListState(): LiveData<List<Track>> = _trackListLiveData

    private val _durationLiveData = MutableLiveData<Long>()
    fun observeDurationState(): LiveData<Long> = _durationLiveData

    private val _deletePlaylistState = MutableLiveData<Boolean>()
    val deletePlaylistState: LiveData<Boolean> = _deletePlaylistState


    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _playlistLiveData.postValue(playlistInteractor.getPlaylist(playlistId))
        }
    }

    fun getTracks(trackIdList: List<Long>) {
        viewModelScope.launch {
            tracks = playlistInteractor.getTracks(trackIdList).reversed()
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

    fun deleteTrack(trackId: Long, playlist: Playlist) {
        viewModelScope.launch{
            playlistInteractor.deleteTrack(trackId, playlist)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
        val jobs = playlist.tracks.map { trackId ->
            async {
                playlistInteractor.checkAndDeleteTrackFromDataBase(playlist.playlistId, trackId)
            }
        }
            jobs.forEach { it.await() }
            playlistInteractor.deletePlaylist(playlist)
            _deletePlaylistState.postValue(true)
        }
    }

}