package com.example.playlistmaker.settings.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.App
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val application: App,
) : AndroidViewModel(application) {

    private val _themeSettingsState = MutableLiveData<ThemeSettings>()
    val themeSettingsState: LiveData<ThemeSettings> = _themeSettingsState

    init {
        _themeSettingsState.postValue(settingsInteractor.getThemeSettings())
    }

    fun showUserAgreement() {
        sharingInteractor.showUserAgreement()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun sendMailToSupport() {
        sharingInteractor.sendMailToSupport()
    }

    fun changeAppTheme(isDarkTheme: Boolean) {
        val settings = ThemeSettings(isDarkTheme)
        _themeSettingsState.postValue(settings)
        settingsInteractor.updateThemeSetting(settings)
        application.switchTheme(isDarkTheme)
    }

}