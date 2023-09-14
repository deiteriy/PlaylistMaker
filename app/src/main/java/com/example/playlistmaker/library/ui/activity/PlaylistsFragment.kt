package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.ui.PlaylistsAdapter
import com.example.playlistmaker.library.ui.models.PlaylistState
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment(), PlaylistsAdapter.OnPlaylistClickListener {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel by viewModel<PlaylistsViewModel>()
    private val playlistsAdapter = PlaylistsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = playlistsAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.newPlaylistButton.setOnClickListener {
            navToCreatePlaylist()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.showPlaylists()
    }
    companion object {
        fun newInstance() = PlaylistsFragment().apply {}
    }

    private fun render(state: PlaylistState) {
        when(state) {
            is PlaylistState.PlaylistFullState -> {
                playlistsAdapter.setPlaylists(state.playlists)
                binding.recyclerView.visibility = View.VISIBLE
                binding.playlistsPlaceholder.visibility = View.GONE
            }
            is PlaylistState.PlaylistEmptyState -> {
                binding.recyclerView.visibility = View.GONE
                binding.playlistsPlaceholder.visibility = View.VISIBLE
            }
        }
    }
    private fun navToCreatePlaylist() {
        val action = LibraryFragmentDirections.actionLibraryFragmentToCreatePlaylistFragment2()
        findNavController().navigate(action)

    }

    private fun navToPlaylist(item: Playlist) {
        val action = LibraryFragmentDirections.actionLibraryFragmentToShowPlaylistFragment(item)
        findNavController().navigate(action)
    }

    override fun onPlaylistClick(item: Playlist) {
        navToPlaylist(item)
    }
}