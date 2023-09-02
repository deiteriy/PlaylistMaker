package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.ui.viewmodel.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var url: String
    private val playlistsAdapter = PlaylistsBottomSheetAdapter()
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args: PlayerActivityArgs by navArgs()
        val track = args.item
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = playlistsAdapter

        viewModel.observePlaylistState().observe(this) {
            playlistsAdapter.setPlaylists(it)
        }

        viewModel.initWithTrack(track)

        url = track.previewUrl!!


        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.durationView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.yearView.text = track.releaseDate?.substringBefore('-')
        binding.genreView.text = track.primaryGenreName
        binding.countryView.text = track.country
        binding.trackProgress.text = getString(R.string.timer_reset)

        val bottomSheetContainer = binding.playlistsBottomSheet
        val overlay = binding.overlay
        val bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehaviour.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        viewModel.showPlaylists()
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })



        fun playbackControl(playerState: PlayerState) {
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

        viewModel.observeState().observe(this) { state ->
            binding.playButton.setOnClickListener {
                playbackControl(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                binding.trackProgress.text = getString(R.string.timer_reset)
                binding.playButton.setImageResource(R.drawable.play_button)
            }
        }

        viewModel.observeIsFavorite().observe(this) {
            if (it == true) {
                binding.likeButton.setImageResource(R.drawable.like_button_active)
            } else {
                binding.likeButton.setImageResource(R.drawable.like_button_inactive)
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
        val albumCover = track.getCoverArtwork()

        Glide.with(binding.trackCover)
            .load(albumCover)
            .transform(RoundedCorners(binding.trackCover.resources.getDimensionPixelSize(R.dimen.player_cover_corner_radius)))
            .placeholder(R.drawable.big_cover_placeholder)
            .into(binding.trackCover)

        binding.arrowBack.setOnClickListener {
            finish()
        }
        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
        binding.saveButton.setOnClickListener {
            bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.newPlaylistButton.setOnClickListener {

        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
        binding.playButton.setImageResource(R.drawable.play_button)

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}