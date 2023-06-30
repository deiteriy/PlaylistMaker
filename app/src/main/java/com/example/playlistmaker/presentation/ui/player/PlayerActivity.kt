package com.example.playlistmaker.presentation.ui.player

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track
import java.util.*

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var url: String
    private lateinit var viewModel: PlayerViewModel

    private var playerState = STATE_DEFAULT

    private fun playbackControl() {
        when (playerState) {
            STATE_PREPARED, STATE_DEFAULT, STATE_PAUSED -> {
                viewModel.play()
                binding.playButton.setImageResource(R.drawable.pause_button)
            }

            STATE_PLAYING -> {
                viewModel.pause()
                binding.playButton.setImageResource(R.drawable.play_button)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]

        val track: Track = intent.getSerializableExtra("item") as Track
        url = track.previewUrl
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.durationView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.yearView.text = track.releaseDate.substringBefore('-')
        binding.genreView.text = track.primaryGenreName
        binding.countryView.text = track.country
        binding.trackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        viewModel.prepare(url)
        binding.playButton.setOnClickListener {
            playbackControl()
        }

        track.previewUrl?.let { viewModel.prepare(it) }

        viewModel.observeState().observe(this) { state ->
            binding.playButton.setOnClickListener {
                playbackControl()
            }
            if (state == PlayerState.STATE_COMPLETE) {
                binding.playButton.setImageResource(R.drawable.pause_button)
            }
        }

        if (track.collectionName.isNullOrEmpty()) {
            binding.albumView.visibility = View.GONE
        } else {
            binding.albumView.text = track.collectionName
        }
        var albumCover = track.getCoverArtwork()

        Glide.with(binding.trackCover)
            .load(albumCover)
            .transform(RoundedCorners(binding.trackCover.resources.getDimensionPixelSize(R.dimen.player_cover_corner_radius)))
            .placeholder(R.drawable.big_cover_placeholder)
            .into(binding.trackCover)

            binding.arrowBack.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }
}