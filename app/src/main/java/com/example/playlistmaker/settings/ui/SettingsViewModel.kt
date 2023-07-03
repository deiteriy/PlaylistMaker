package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.SendMailToSupport
import com.example.playlistmaker.settings.domain.usecase.ShareAppUseCase
import com.example.playlistmaker.settings.domain.usecase.ShowUserAgreement

class SettingsViewModel(
    private val changeAppTheme: ChangeAppTheme,
    private val sendMailToSupport: SendMailToSupport,
    private val shareAppUseCase: ShareAppUseCase,
    private val showUserAgreement: ShowUserAgreement
) : ViewModel() {

    fun showUserAgreement() {
        showUserAgreement.execute()
    }

    fun shareApp() {
        shareAppUseCase.execute()
    }

    fun sendMailToSupport() {
        sendMailToSupport.execute()
    }

    fun changeAppTheme() {
        changeAppTheme.execute()
    }

}