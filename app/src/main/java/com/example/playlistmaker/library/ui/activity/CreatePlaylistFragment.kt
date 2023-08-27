package com.example.playlistmaker.library.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.library.ui.viewmodels.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CreatePlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    private lateinit var viewModel: CreatePlaylistViewModel
    private lateinit var binding: FragmentCreatePlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.titleEditText.isActivated = false
                    binding.titleTop.visibility = View.INVISIBLE
                    binding.createButton.isEnabled = false
                } else {
                    binding.titleEditText.isActivated = true // Enable the EditText
                    binding.titleTop.visibility = View.VISIBLE
                    binding.createButton.isEnabled = true
                }
            }
        })


        binding.descriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.descriptionEditText.isActivated = false
                    binding.descriptionTop.visibility = View.INVISIBLE
                } else {
                    binding.descriptionEditText.isActivated = true
                    binding.descriptionTop.visibility = View.VISIBLE
                }
            }
        })


        binding.arrowBack.setOnClickListener {
            if(isFieldsEmpty()) {
                findNavController().navigateUp()
            } else {
                showDialogue()
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.shapeRectangle.setImageURI(uri)

                    //        saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.shapeRectangle.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createButton.setOnClickListener {
            Toast.makeText(requireContext(), "Плейлист ${binding.titleEditText.text} создан", Toast.LENGTH_SHORT)
                .show()
            findNavController().navigateUp()
        }
    }

    private fun isFieldsEmpty(): Boolean {
        return binding.titleEditText.text.isBlank() && binding.descriptionEditText.text.isBlank()
    }
    private fun showDialogue() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.create_playlist_finish)
            .setMessage(R.string.unsaved_changes)
            .setNegativeButton(R.string.cancel) { dialog, which ->
            }
            .setPositiveButton(R.string.finish) { dialog, which ->
                findNavController().navigateUp()
            }
            .show()
    }
}