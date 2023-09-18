package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.ui.viewmodels.EditPlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {
    override val viewModel by viewModel<EditPlayListViewModel>()
    lateinit var playlist: Playlist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: EditPlaylistFragmentArgs by navArgs()
        val playlistId: Long = args.item
        viewModel.getPlaylist(playlistId)
        viewModel.observePlaylist().observe(viewLifecycleOwner) {
            playlist = it
            if(playlist !=null) {
                initPlaylistBindings()
            }
        }

        binding.createButton.text = getString(R.string.save)
        binding.newPlaylistTitle.text = getString(R.string.edit)


        binding.createButton.setOnClickListener {
            saveEditPlaylist(playlist)
        }

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
    }

    private fun initPlaylistBindings() {
        binding.titleEditText.setText(playlist.name)
        binding.descriptionEditText.setText(playlist.description)
        if (playlist.playlistCover.toString() != "null" && playlist.playlistCover != null) {
            binding.shapeRectangle.setImageURI(playlist.playlistCover)
        }
    }

    private fun saveEditPlaylist(playlist: Playlist) {
        playlist.name = binding.titleEditText.text.toString()
        playlist.description = binding.descriptionEditText.text.toString()
        viewModel.saveEditPlaylist(playlist)
        findNavController().popBackStack()
    }
}