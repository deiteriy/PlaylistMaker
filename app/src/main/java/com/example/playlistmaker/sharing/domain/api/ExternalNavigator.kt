package com.example.playlistmaker.sharing.domain.api

import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun showUserAgreement(link: String)
    fun shareApp(link: String)
    fun sendMailToSupport(emailData: EmailData)
}