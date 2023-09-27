package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.library.ui.models.FavoriteState
import com.example.playlistmaker.library.ui.viewmodels.FavoritesViewModel
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.ui.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment(): Fragment(), TrackListAdapter.OnTrackClickListener {
        private lateinit var binding: FragmentFavoritesBinding
        private val favoritesAdapter = TrackListAdapter(this)
        private val viewModel by viewModel<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteTracks.adapter = favoritesAdapter
        binding.favoriteTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.findFavoriteTrack()
    }

    private fun render(state: FavoriteState) {
        when(state) {
                is FavoriteState.FavoriteFullState -> {
                    favoritesAdapter.setTracks(state.tracks)
                    binding.favoriteTracks.visibility = View.VISIBLE
                    binding.libraryPlaceholder.visibility = View.GONE
                }
                is FavoriteState.FavoriteEmptyState -> {
                    binding.favoriteTracks.visibility = View.GONE
                    binding.libraryPlaceholder.visibility = View.VISIBLE
                    binding.libraryPlaceholderText.text = resources.getString(R.string.library_is_empty)
                    binding.libraryButton.visibility = View.INVISIBLE
                }

            }


    }

    companion object {
        fun newInstance() = FavoritesFragment().apply {}
    }

    override fun onTrackClick(item: Track) {
        if (viewModel.clickDebounce()) {
            try {
                viewModel.addTrack(item)
                navToTrack(item)
            } catch (e: NullPointerException) {
                Toast.makeText(requireContext(), R.string.preview_not_found, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun navToTrack(item: Track) {
        val action = LibraryFragmentDirections.actionLibraryFragmentToPlayerFragment(item)
        findNavController().navigate(action)
    }


}