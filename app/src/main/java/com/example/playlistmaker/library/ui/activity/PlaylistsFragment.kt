package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.ui.PlaylistsAdapter
import com.example.playlistmaker.library.ui.models.PlaylistState
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel by viewModel<PlaylistsViewModel>()
    private val playlistsAdapter = PlaylistsAdapter()

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
        Log.i("SHOWPLAYLIST", "Сработал метод onViewCreated в PlaylistFragment")
        viewModel.showPlaylists()




        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = playlistsAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.newPlaylistButton.setOnClickListener {
            navToPlaylist()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.showPlaylists()
        Log.i("SHOWPLAYLIST", "Сработал метод onStart в PlaylistFragment")

    }
    override fun onResume() {
        super.onResume()
        Log.i("SHOWPLAYLIST", "Сработал метод onResume в PlaylistFragment")
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
    private fun navToPlaylist() {
        val action = LibraryFragmentDirections.actionLibraryFragmentToCreatePlaylistFragment2()
        findNavController().navigate(action)

    }
}