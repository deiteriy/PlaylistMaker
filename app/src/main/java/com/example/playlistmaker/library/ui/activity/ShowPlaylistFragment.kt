package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
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
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerFragmentArgs
import com.example.playlistmaker.search.ui.TrackListAdapter


class ShowPlaylistFragment : Fragment(), TrackListAdapter.OnTrackClickListener {

    private lateinit var binding: FragmentShowPlaylistBinding
    private val trackListAdapter = TrackListAdapter(this)

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

        val playlist: Playlist = args.item
        val tracks = playlist.tracks

        binding.playlistTitle.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.count.text = playlist.tracksCount.toString()
        binding.duration.text = "Примерно 100 часов"

        Glide.with(binding.playlistCover)
            .load(playlist.playlistCover)
            .placeholder(R.drawable.big_cover_placeholder)
            .into(binding.playlistCover)

        binding.recyclerView.adapter = trackListAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)



        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onTrackClick(item: Track) {
        Toast.makeText(requireContext(), "Прочитан клик ${item.trackName}", Toast.LENGTH_SHORT)
            .show()
    }


}