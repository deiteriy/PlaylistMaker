package com.example.playlistmaker.di

import com.example.playlistmaker.App
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(interactor = get())
    }

    viewModel {
        SettingsViewModel(sharingInteractor = get(), settingsInteractor =  get(), androidApplication() as App)
    }

    viewModel {(track: Track) ->
        PlayerViewModel(playerInteractor = get(), track = track)
    }

}