package com.example.playlistmaker.library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.ui.models.FavoriteState
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor): ViewModel() {

    init {findFavoriteTrack()}

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


    fun observeFavorites(): LiveData<FavoriteState> = _favoritesLiveData
}