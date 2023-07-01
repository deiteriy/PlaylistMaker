package com.example.playlistmaker.presentation.ui.player

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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


    private fun playbackControl(playerState: PlayerState) {
        when (playerState) {
            PlayerState.STATE_PREPARED, PlayerState.STATE_COMPLETE, PlayerState.STATE_PAUSED -> {
                viewModel.play()
                binding.playButton.setImageResource(R.drawable.pause_button)
            }

            PlayerState.STATE_PLAYING -> {
                viewModel.pause()
                binding.playButton.setImageResource(R.drawable.play_button)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val track: Track = intent.getSerializableExtra("item") as Track
        url = track.previewUrl

        viewModel = PlayerViewModelFactory(track).create(PlayerViewModel::class.java)


        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.durationView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.yearView.text = track.releaseDate?.substringBefore('-')
        binding.genreView.text = track.primaryGenreName
        binding.countryView.text = track.country
        binding.trackProgress.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)



        viewModel.observeState().observe(this) { state ->
            binding.playButton.setOnClickListener {
                playbackControl(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                binding.playButton.setImageResource(R.drawable.play_button)
            }
        }

        viewModel.observeTime().observe(this) { time ->
            binding.trackProgress.text = time
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
}