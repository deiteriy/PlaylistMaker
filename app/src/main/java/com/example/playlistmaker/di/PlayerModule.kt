package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.api.PlayerRepository
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    single {
        MediaPlayer()
    }
    single<PlayerRepository> {
        PlayerRepositoryImpl(mediaPlayer = get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl(repository = get())
    }

    viewModel {
        PlayerViewModel(playerInteractor = get())
    }
}