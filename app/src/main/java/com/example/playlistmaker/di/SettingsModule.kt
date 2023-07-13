package com.example.playlistmaker.di

import com.example.playlistmaker.App
import com.example.playlistmaker.settings.data.api.SettingsRepository
import com.example.playlistmaker.settings.data.api.SettingsThemeStorage
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.impl.SettingsThemeStorageImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.ui.SettingsViewModel
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    single<SettingsThemeStorage> {
        SettingsThemeStorageImpl(sharedPreferences = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(storage = get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(externalNavigator = get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(repository = get())
    }

    viewModel {
        SettingsViewModel(sharingInteractor = get(), settingsInteractor =  get(), androidApplication() as App)
    }
}