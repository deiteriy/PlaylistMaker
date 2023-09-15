package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentShowPlaylistBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.ui.viewmodels.FavoritesViewModel
import com.example.playlistmaker.library.ui.viewmodels.ShowPlaylistViewModel
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerFragmentArgs
import com.example.playlistmaker.search.ui.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class ShowPlaylistFragment : Fragment(), TrackListAdapter.OnTrackClickListener {

    private lateinit var binding: FragmentShowPlaylistBinding
    private val trackListAdapter = TrackListAdapter(this)
    private val viewModel by viewModel<ShowPlaylistViewModel>()
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

        viewModel.observeState().observe(viewLifecycleOwner) {
            if (it != null) {
                playlist = it
                initializeBindings(playlist)
                viewModel.getTracks(playlist.tracks)
            }
        }

        viewModel.observeTrackListState().observe(viewLifecycleOwner) {
            if (it != null) {
                trackListAdapter.setTracks(it)
                viewModel.getDuration()
            }
        }

        viewModel.observeDurationState().observe(viewLifecycleOwner) {
            if(it != null) {
                val duration = SimpleDateFormat("mm", Locale.getDefault()).format(it).toInt()
                val formattedDuration = resources.getQuantityString(R.plurals.minutes, duration, duration)
                binding.duration.text = formattedDuration
            }
        }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onTrackClick(item: Track) {
        Toast.makeText(requireContext(), "Прочитан клик ${item.trackName}", Toast.LENGTH_SHORT)
            .show()
    }


    private fun initializeBindings(playlist: Playlist) {
        binding.playlistTitle.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.count.text = resources.getQuantityString(R.plurals.tracks, playlist.tracksCount.toInt(), playlist.tracksCount.toInt())

        Glide.with(binding.playlistCover)
            .load(playlist.playlistCover)
            .placeholder(R.drawable.big_cover_placeholder)
            .into(binding.playlistCover)

        binding.recyclerView.adapter = trackListAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }


}