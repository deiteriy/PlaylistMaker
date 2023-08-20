package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.domain.NetworkError
import com.example.playlistmaker.search.ui.model.SearchState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackListAdapter.OnTrackClickListener {

    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var binding: FragmentSearchBinding
    private val trackListAdapter = TrackListAdapter(this)
    private val historyAdapter = TrackListAdapter(this)
    var textInSearch: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchPlaceholder.visibility = View.GONE
        binding.rvTrackList.adapter = trackListAdapter
        binding.rvTrackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                textInSearch = s.toString()
                if (binding.inputSearch.hasFocus() && s?.isEmpty() == true) {
                    viewModel.showHistory()
                } else if (s?.isEmpty() == false) {
                    skipHistory()
                }
                s?.let { viewModel.searchDebounce(it.toString()) }

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.inputSearch.addTextChangedListener(searchTextWatcher)

        binding.searchButton.setOnClickListener {
            viewModel.findTrack(textInSearch)
        }

        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
            skipHistory()
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputSearch.text.isEmpty()) showHistory(
            )
        }

        binding.clearIcon.setOnClickListener {
            binding.inputSearch.setText("")
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
                0
            )
            trackListAdapter.setTracks(null)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    override fun onTrackClick(item: Track) {
        if(item.previewUrl == null) {
            Toast.makeText(requireContext(), R.string.preview_not_found, Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (viewModel.clickDebounce()) {
            try {
                viewModel.saveTrack(item)
                navToTrack(item)
            } catch (e: NullPointerException) {
                Toast.makeText(requireContext(), R.string.preview_not_found, Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun updateHistory(history: List<Track>) {
        historyAdapter.setTracks(history)
    }
    private fun showHistory() {
        if (historyAdapter.data.isNotEmpty()) {
            binding.searchPlaceholder.visibility = View.GONE
            binding.trackList.visibility = View.VISIBLE
            binding.rvTrackList.visibility = View.VISIBLE
            binding.clearHistory.visibility = View.VISIBLE
            binding.searchHistoryText.visibility = View.VISIBLE
            binding.rvTrackList.adapter = historyAdapter
        }
    }

    private fun skipHistory() {
        binding.clearHistory.visibility = View.GONE
        binding.searchHistoryText.visibility = View.GONE
        binding.rvTrackList.visibility = View.GONE
    }

    private fun showHolder(text: Int, image: Int, button: Boolean) {
        trackListAdapter.setTracks(null)
        binding.searchPlaceholderText.setText(text)
        binding.searchPlaceholderImage.setImageResource(image)
        if (button) binding.searchButton.visibility =
            View.VISIBLE else binding.searchButton.visibility = View.GONE
        binding.trackList.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.VISIBLE
    }

    private fun showSearchResult(tracks: List<Track>) {
        clearAll()
        trackListAdapter.setTracks(tracks)
        binding.rvTrackList.adapter = trackListAdapter
        binding.rvTrackList.visibility = View.VISIBLE
        binding.trackList.visibility = View.VISIBLE
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.SearchHistory -> {
                updateHistory(state.tracks)
                if(historyAdapter.data.isNotEmpty()) {
                    showHistory()
                }
            }

            is SearchState.Loading -> {
                showLoading()
            }

            is SearchState.SearchedTracks -> {
                skipHistory()
                binding.rvTrackList.adapter = trackListAdapter
                showSearchResult(state.tracks)
            }

            is SearchState.SearchError -> {
                showErrorMessage(state.error)
            }
        }
    }


    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvTrackList.visibility = View.GONE
    }

    private fun clearAll() {
        binding.trackList.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.clearHistory.visibility = View.GONE
        binding.searchHistoryText.visibility = View.GONE
        binding.rvTrackList.visibility = View.GONE
        binding.searchPlaceholder.visibility = View.GONE
    }

    private fun showErrorMessage(error: NetworkError) {
        clearAll()
        when (error) {
            NetworkError.NOTHING_FOUND -> {
                showHolder(
                    R.string.nothing_found,
                    R.drawable.nothing_found, false
                )
            }

            NetworkError.NO_INTERNET -> {
                showHolder(
                    R.string.something_went_wrong,
                    R.drawable.no_internet, true
                )
            }
        }
    }

    private fun navToTrack(item: Track) {
        val action = SearchFragmentDirections.actionSearchFragmentToPlayerActivity(item)
        findNavController().navigate(action)
    }

}