package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.viewmodels.FavoritesViewModel
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
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