package com.example.playlistmaker.sharing.data

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.data.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {


    override fun showUserAgreement(link: String) {
        val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        browseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, browseIntent, null)
    }

    override fun shareApp(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
        startActivity(context, shareIntent, null)
    }

    override fun sendMailToSupport(emailData: EmailData) {
        val message = context.getString(R.string.support_message)
        val subject = context.getString(R.string.support_subject)


        try {
            val mailToSupportIntent = Intent(Intent.ACTION_SENDTO)
            mailToSupportIntent.data = Uri.parse(emailData.mailTo)
            mailToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.mail))
            mailToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            mailToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            mailToSupportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, mailToSupportIntent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.mail_client_not_found, Toast.LENGTH_SHORT).show()
        }
    }
}