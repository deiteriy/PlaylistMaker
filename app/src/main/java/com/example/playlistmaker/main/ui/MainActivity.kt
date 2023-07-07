package com.example.playlistmaker.main.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.LibraryActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this, MainViewModelFactory(this)).get(MainViewModel::class.java)

        val search = findViewById<Button>(R.id.search)
        search.setOnClickListener {
            /*val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)*/
            viewModel.openSearchActivity()
        }

        val library = findViewById<Button>(R.id.library)
        library.setOnClickListener {
            /*val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)*/
            viewModel.openLibraryActivity()
        }

        val settings = findViewById<Button>(R.id.settings)
        settings.setOnClickListener {
            /*val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)*/
            viewModel.openSettingsActivity()
        }

    }

}