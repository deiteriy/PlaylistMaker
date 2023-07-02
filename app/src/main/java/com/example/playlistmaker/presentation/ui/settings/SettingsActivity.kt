package com.example.playlistmaker.presentation.ui.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  val sharedPreferences = getSharedPreferences(DARK_THEME, MODE_PRIVATE)
     //   var isDarkTheme: Boolean? = sharedPreferences.getBoolean(DARK_THEME, null) ?: return Boolean
        setContentView(R.layout.activity_settings)

        val returnArrow = findViewById<ImageView>(R.id.arrow_back)
        returnArrow.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }


        val shareApp = findViewById<TextView>(R.id.share_app)
        shareApp.setOnClickListener {
            val message = getString(R.string.share_app_link)
            val shareTitle = getString(R.string.share_title)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.type = "text/plain"
            startActivity(Intent.createChooser(shareIntent, shareTitle))
        }

        val mailToSupport = findViewById<TextView>(R.id.support)
        mailToSupport.setOnClickListener {try {
            val message = getString(R.string.support_message)
            val subject = getString(R.string.support_subject)
            val mailToSupportIntent = Intent(Intent.ACTION_SENDTO)
            val userEmail = getString(R.string.user_email)
            mailToSupportIntent.data = Uri.parse("mailto:")
            mailToSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(userEmail))
            mailToSupportIntent.putExtra(Intent.EXTRA_TEXT, message)
            mailToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            startActivity(mailToSupportIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, R.string.mail_client_not_found, Toast.LENGTH_SHORT).show()
        }


        }

        val showUserAgreement = findViewById<TextView>(R.id.user_agreement)
        showUserAgreement.setOnClickListener {
            val link = getString(R.string.user_agreement_link)
            val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browseIntent)
        }


    }
}