package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import java.util.*

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var url: String
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT


    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

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


        preparePlayer()
        binding.playButton.setOnClickListener {
            Log.i("playit", "Получен клик по кнопке плей")
            playbackControl()
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
        Log.i("album_cover", "Ссылка: $albumCover")
        binding.arrowBack.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}