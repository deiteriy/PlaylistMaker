package com.example.playlistmaker.library.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentShowPlaylistBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.ui.viewmodels.ShowPlaylistViewModel
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.TrackListInPlaylistAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class ShowPlaylistFragment : Fragment(), TrackListInPlaylistAdapter.OnTrackClickListener {

    private lateinit var binding: FragmentShowPlaylistBinding
    private val trackListAdapter = TrackListInPlaylistAdapter(this)
    private val viewModel by viewModel<ShowPlaylistViewModel>()
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var menuBottomSheetBehaviour: BottomSheetBehavior<LinearLayout>
    private lateinit var playlist: Playlist
    private lateinit var trackList: List<Track>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ShowPlaylistFragmentArgs by navArgs()

        val playlistId = args.item
        viewModel.getPlaylist(playlistId)
        bottomSheetContainer = binding.playlistsMenuBottomSheet
        menuBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        val overlay = binding.overlay



        menuBottomSheetBehaviour.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        viewModel.observeState().observe(viewLifecycleOwner) {
            if (it != null) {
                playlist = it
                initializeBindings(playlist)
                viewModel.getTracks(playlist.tracks)
                showNoTracksMessage(playlist.tracks.isEmpty())
            }
        }

        viewModel.observeTrackListState().observe(viewLifecycleOwner) {
            if (it != null) {
                trackListAdapter.setTracks(it)
                trackList = it
                viewModel.getDuration()
            }
        }

        viewModel.observeDurationState().observe(viewLifecycleOwner) {
            if (it != null) {
                val duration = SimpleDateFormat("mm", Locale.getDefault()).format(it).toInt()
                val formattedDuration =
                    resources.getQuantityString(R.plurals.minutes, duration, duration)
                binding.duration.text = formattedDuration
            }
        }
        viewModel.deletePlaylistState.observe(viewLifecycleOwner) {
            if (true) {
                findNavController().popBackStack()
            }
        }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.openMenu.setOnClickListener {
            openMenuBottomSheet()
        }

        binding.deletePlaylist.setOnClickListener {
            closeMenuBottomSheet()
            showDeletePlaylistDialogue()
        }

        binding.shareIcon.setOnClickListener {
            sharePlaylist()
        }

        binding.shareMenu.setOnClickListener {
            sharePlaylist()
        }

        binding.editInformation.setOnClickListener {
            navToEditPlaylist(playlist.playlistId)
        }

    }

    override fun onTrackClick(item: Track) {
        navToTrack(item)
    }

    override fun onTrackLongClick(item: Track) {
        showDeleteTrackDialogue(item.trackId, playlist)
    }


    private fun initializeBindings(playlist: Playlist) {
        binding.playlistTitle.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.count.text = resources.getQuantityString(
            R.plurals.tracks,
            playlist.tracksCount.toInt(),
            playlist.tracksCount.toInt()
        )
        binding.playlistTitleBs.text = playlist.name
        binding.playlistTracksCountBs.text = binding.count.text


        Glide.with(binding.playlistCover)
            .load(playlist.playlistCover)
            .placeholder(R.drawable.big_cover_placeholder)
            .into(binding.playlistCover)

        Glide.with(binding.playlistCoverBs)
            .load(playlist.playlistCover)
            .placeholder(R.drawable.big_cover_placeholder)
            .into(binding.playlistCoverBs)

        binding.recyclerView.adapter = trackListAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        val openMenu = binding.openMenu
        val screenHeight = resources.displayMetrics.heightPixels

        openMenu.post {
            val openMenuLocation = IntArray(2)
            openMenu.getLocationOnScreen(openMenuLocation)

            val openMenuHeightFromBottom = screenHeight - openMenuLocation[1] - resources.getDimensionPixelSize(R.dimen.margin_8)

            val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
            bottomSheetBehavior.peekHeight = openMenuHeightFromBottom
        }
    }

    private fun navToTrack(track: Track) {
        val action =
            ShowPlaylistFragmentDirections.actionShowPlaylistFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    private fun navToEditPlaylist(item: Long) {
        val action = ShowPlaylistFragmentDirections.actionShowPlaylistFragmentToEditPlaylistFragment(item)
        findNavController().navigate(action)
    }

    private fun sharePlaylist() {
        if (playlist.tracks.isEmpty()) {
            Toast.makeText(requireContext(), R.string.no_tracks, Toast.LENGTH_SHORT)
                .show()
        } else {
            var message = "${playlist.name}\n${playlist.description}\n${binding.playlistTracksCountBs.text}\n"
            var count = 1
            for(track in trackList) {
                val trackMessage = "${count}. ${track.artistName} - ${track.trackName} (${SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)})\n"
                message += trackMessage
                count += 1
            }

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            }
            ContextCompat.startActivity(requireContext(), shareIntent, null)

        }

    }

    private fun showDeleteTrackDialogue(trackId: Long, playlist: Playlist) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_track)
            .setMessage(R.string.delete_track_are_you_sure)
            .setNegativeButton(R.string.cancel) { dialog, which ->
            }
            .setPositiveButton(R.string.delete) { dialog, which ->
                viewModel.deleteTrack(trackId, playlist)
                viewModel.getPlaylist(playlist.playlistId)
            }
            .show()
    }

    private fun showNoTracksMessage(isTrackListEmpty: Boolean) {
        if(isTrackListEmpty) {
            binding.noTracksMessage.visibility = View.VISIBLE
        } else {
            binding.noTracksMessage.visibility = View.GONE
        }
    }

    private fun showDeletePlaylistDialogue() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_playlist))
            .setMessage(getString(R.string.delete_playlist_are_you_sure))
            .setNegativeButton(R.string.cancel) { dialog, which ->
            }
            .setPositiveButton(R.string.delete) { dialog, which ->
                viewModel.deletePlaylist(playlist = playlist)
            }
            .show()
    }

    private fun openMenuBottomSheet() {
        menuBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun closeMenuBottomSheet() {
        menuBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }


}