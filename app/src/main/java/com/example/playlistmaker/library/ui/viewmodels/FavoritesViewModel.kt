package com.example.playlistmaker.library.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.ui.models.FavoriteState
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.ui.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor): ViewModel() {
    private var isClickAllowed = true

    val _favoritesLiveData = MutableLiveData<FavoriteState>()

    fun findFavoriteTrack() {
        viewModelScope.launch {
            favoritesInteractor.favoriteTracks().collect {
                if(it.isEmpty()) {
                    _favoritesLiveData.postValue(FavoriteState.FavoriteEmptyState())
                } else {
                    _favoritesLiveData.postValue(FavoriteState.FavoriteFullState(it))
                }
            }
        }
    }


    fun observeState(): LiveData<FavoriteState> = _favoritesLiveData

    fun addTrack(track: Track) {
        viewModelScope.launch {
            favoritesInteractor.markAsFavorite(track)
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(SearchViewModel.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}