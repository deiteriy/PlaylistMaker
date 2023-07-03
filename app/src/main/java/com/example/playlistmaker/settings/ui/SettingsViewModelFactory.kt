package com.example.playlistmaker.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.SendMailToSupport
import com.example.playlistmaker.settings.domain.usecase.ShareAppUseCase
import com.example.playlistmaker.settings.domain.usecase.ShowUserAgreement

class SettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val settingsRepository by lazy {
        SettingsRepositoryImpl(context)
    }

    private val sendMailToSupport by lazy {
        SendMailToSupport(settingsRepository)
    }

    private val shareAppUseCase by lazy {
        ShareAppUseCase(settingsRepository)
    }

    private val changeAppTheme by lazy {
        ChangeAppTheme(settingsRepository)
    }
    private val showUserAgreement by lazy {
        ShowUserAgreement(settingsRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            changeAppTheme = changeAppTheme,
            sendMailToSupport = sendMailToSupport,
            shareAppUseCase = shareAppUseCase,
            showUserAgreement = showUserAgreement
        ) as T
    }
}