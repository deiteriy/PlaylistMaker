package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val search = findViewById<Button>(R.id.search)
        search.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val library = findViewById<Button>(R.id.library)
        library.setOnClickListener {
            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }

        val settings = findViewById<Button>(R.id.settings)
        settings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

    }

}