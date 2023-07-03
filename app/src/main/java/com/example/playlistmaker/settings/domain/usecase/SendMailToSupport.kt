package com.example.playlistmaker.settings.domain.usecase

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.api.SettingsRepository

class SendMailToSupport(private val repository: SettingsRepository) {

    fun execute() {
        repository.sendMailToSupport()
    }
}