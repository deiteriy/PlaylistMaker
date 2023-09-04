package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.viewmodel.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlayerFragment : Fragment(), PlaylistsBottomSheetAdapter.OnPlaylistClickListener {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var url: String
    lateinit var track: Track
    private val playlistsAdapter = PlaylistsBottomSheetAdapter(this)
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: PlayerFragmentArgs by navArgs()

        track = args.item
        binding.recyclerView.adapter = playlistsAdapter

        viewModel.observePlaylistState().observe(requireActivity()) {
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

        viewModel.observeState().observe(requireActivity()) { state ->
            binding.playButton.setOnClickListener {
                playbackControl(state)
            }
            if (state == PlayerState.STATE_COMPLETE) {
                binding.trackProgress.text = getString(R.string.timer_reset)
                binding.playButton.setImageResource(R.drawable.play_button)
            }
        }

        viewModel.observeIsFavorite().observe(requireActivity()) {
            if (it == true) {
                binding.likeButton.setImageResource(R.drawable.like_button_active)
            } else {
                binding.likeButton.setImageResource(R.drawable.like_button_inactive)
            }
        }

        viewModel.observeTime().observe(requireActivity()) { time ->
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
            findNavController().popBackStack()
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

    override fun onPlaylistClick(item: Playlist) {
        Log.i("CLICKLOOK", "Попали в onPlaylistClick")
        if (viewModel.clickDebounce()) {
            if (!viewModel.isInPlaylist(playlist = item, trackId = track.trackId)) {
                viewModel.addToPlaylist(playlist = item, track = track)
                Toast.makeText(requireContext(), "Трек добавлен в ${item.name}", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Трек уже есть в ${item.name}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}