package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.viewmodels.FavoritesViewModel
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {

    viewModel {
        PlaylistsViewModel()
    }

    viewModel() {
        FavoritesViewModel()
    }
}