package com.example.playlistmaker.settings.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.creator.Creator

class SettingsViewModelFactory(private val context: Context,  application: App) : ViewModelProvider.Factory {

    init {
        Creator.init(context)
    }

    private val application: App = application

    private val sharingInteractor by lazy {
        Creator.provideSharingInteractor()
    }
    private val settingsInteractor by lazy {
        Creator.provideSettingsInteractor()
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            sharingInteractor = sharingInteractor,
            settingsInteractor = settingsInteractor,
            application = application
        ) as T
    }
}