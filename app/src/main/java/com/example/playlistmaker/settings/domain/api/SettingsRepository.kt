package com.example.playlistmaker.settings.domain.api

interface SettingsRepository {
    fun showUserAgreement()
    fun shareApp()
    fun sendMailToSupport()
    fun changeAppTheme()
}