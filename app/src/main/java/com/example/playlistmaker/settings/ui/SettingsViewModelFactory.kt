package com.example.playlistmaker.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App


class SettingsViewModelFactory(private val context: Context,  application: App) : ViewModelProvider.Factory {

   /* init {
        Creator.init(context)
    }

    private val application: App = application

    private val sharingInteractor by lazy {
        Creator.provideSharingInteractor(context)
    }
    private val settingsInteractor by lazy {
        Creator.provideSettingsInteractor(context)
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            sharingInteractor = sharingInteractor,
            settingsInteractor = settingsInteractor,
            application = application
        ) as T
    }*/
}