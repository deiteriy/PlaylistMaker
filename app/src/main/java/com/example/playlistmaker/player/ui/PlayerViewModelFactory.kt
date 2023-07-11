package com.example.playlistmaker.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.models.Track

class PlayerViewModelFactory(private val track: Track): ViewModelProvider.Factory {
   /* val interactor = Creator.providePlayerInteractor()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(track, interactor) as T
    }*/
}