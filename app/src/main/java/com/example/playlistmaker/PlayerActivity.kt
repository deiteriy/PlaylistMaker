package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import java.util.*

class PlayerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        val track: Track = intent.getSerializableExtra("item") as Track

        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.durationView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.yearView.text = track.releaseDate
        binding.genreView.text = track.primaryGenreName
        binding.countryView.text = track.country

        if (track.collectionName.isNullOrEmpty()) {
            binding.albumView.visibility = View.GONE
        } else {
            binding.albumView.text = track.collectionName
        }
        var albumCover = track.getCoverArtwork()

        Glide.with(binding.trackCover)
            .load(albumCover)
         //   .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.cover_corner_radius)))
            .placeholder(R.drawable.albumcover_placeholder)
            .into(binding.trackCover)
        Log.i("album_cover", "Ссылка: $albumCover")
        binding.arrowBack.setOnClickListener {
            finish()
        }
    }
}