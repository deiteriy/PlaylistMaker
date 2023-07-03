package com.example.playlistmaker.settings.data

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {


    override fun showUserAgreement() {
        val link = context.getString(R.string.user_agreement_link)
        val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(context, browseIntent, null)
    }

    override fun shareApp() {
        val message = context.getString(R.string.share_app_link)
        val shareTitle = context.getString(R.string.share_title)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.type = "text/plain"
        startActivity(context, Intent.createChooser(shareIntent, shareTitle), null)
    }

    override fun sendMailToSupport() {
        val message = context.getString(R.string.support_message)
        val subject = context.getString(R.string.support_subject)
        val userEmail = context.getString(R.string.user_email)

        try {
            val mailToSupportIntent = Intent(Intent.ACTION_SENDTO)
            mailToSupportIntent.data = Uri.parse("mailto:")
            mailToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(userEmail))
            mailToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            mailToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            startActivity(context, mailToSupportIntent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.mail_client_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    override fun changeAppTheme() {
/*
        TODO("Not yet implemented")
*/
    }
}