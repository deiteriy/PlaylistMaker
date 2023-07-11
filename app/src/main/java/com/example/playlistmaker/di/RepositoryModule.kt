package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.api.PlayerRepository
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.api.SearchRepository
import com.example.playlistmaker.settings.data.api.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import org.koin.dsl.module

val searchRepositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), searchHistory =  get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(mediaPlayer = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(storage = get())
    }
}