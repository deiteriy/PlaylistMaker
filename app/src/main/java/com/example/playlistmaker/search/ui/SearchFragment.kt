package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
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
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.NetworkError
import com.example.playlistmaker.search.ui.model.SearchState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackListAdapter.OnTrackClickListener {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    lateinit var trackListAdapter: TrackListAdapter
    lateinit var historyAdapter: TrackListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackListAdapter = TrackListAdapter(this)
        historyAdapter = TrackListAdapter(this)

        var textInSearch: String = ""

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                textInSearch = s.toString()
                if (binding.inputSearch.hasFocus() && s?.isEmpty() == true && viewModel.showHistory()
                        .isNotEmpty()
                ) {
                    showHistory(viewModel.showHistory())
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

        binding.clearIcon.setOnClickListener {
            viewModel.clearHistory()
            skipHistory()
        }

        binding.inputSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputSearch.text.isEmpty()) showHistory(
                viewModel.showHistory()
            )
        }

        binding.clearIcon.setOnClickListener {
            binding.inputSearch.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            trackListAdapter.setTracks(null)
        }

        binding.searchButton.setOnClickListener {
            viewModel.findTrack(textInSearch)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onTrackClick(item: Track) {
        if (viewModel.clickDebounce()) {
            try {
                viewModel.saveTrack(item)
                val playerIntent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra("item", item)
                }
                startActivity(playerIntent)
            } catch (e: NullPointerException) {
                Toast.makeText(requireContext(), R.string.preview_not_found, Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun skipHistory() {
        binding.clearHistory.visibility = View.GONE
        binding.searchHistoryText.visibility = View.GONE
        binding.rvTrackList.visibility = View.GONE
    }

    private fun showHistory(history: List<Track>) {
        historyAdapter.setTracks(history)
        if (historyAdapter.data.isNotEmpty()) {
            binding.searchPlaceholder.visibility = View.GONE
            binding.trackList.visibility = View.VISIBLE
            binding.rvTrackList.visibility = View.VISIBLE
            binding.clearHistory.visibility = View.VISIBLE
            binding.searchHistoryText.visibility = View.VISIBLE
            binding.rvTrackList.adapter = historyAdapter
        }
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

    private fun showSearchResult(tracks: List<Track>) {
        clearAll()
        trackListAdapter.setTracks(tracks)
        binding.rvTrackList.adapter = trackListAdapter
        binding.rvTrackList.visibility = View.VISIBLE
        binding.trackList.visibility = View.VISIBLE
        Log.i("SEARCHLOG", "Вызвана функция showSearchResult}")
    }


    private fun render(state: SearchState) {
        when (state) {
            is SearchState.SearchHistory -> {
                showHistory(state.tracks)
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
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}


