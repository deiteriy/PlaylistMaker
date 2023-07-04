package com.example.playlistmaker.settings.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(this, applicationContext as App)
        ).get(SettingsViewModel::class.java)

        val returnArrow = findViewById<ImageView>(R.id.arrow_back)
        returnArrow.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        viewModel.themeSettingsState.observe(this) { themeSettings ->
            themeSwitcher.isChecked = themeSettings.darkTheme!!
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.changeAppTheme(checked)
        }

        val shareApp = findViewById<TextView>(R.id.share_app)
        shareApp.setOnClickListener {
            viewModel.shareApp()
        }

        val mailToSupport = findViewById<TextView>(R.id.support)
        mailToSupport.setOnClickListener {
            viewModel.sendMailToSupport()
        }

        val showUserAgreement = findViewById<TextView>(R.id.user_agreement)
        showUserAgreement.setOnClickListener {
            viewModel.showUserAgreement()
        }


    }
}