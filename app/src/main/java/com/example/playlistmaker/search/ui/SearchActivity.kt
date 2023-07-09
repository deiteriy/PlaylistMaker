package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.local.SearchHistoryImpl
import com.example.playlistmaker.search.data.local.TRACK_HISTORY
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.NetworkError
import com.example.playlistmaker.search.ui.model.SearchState
import retrofit2.*
import java.lang.NullPointerException

class SearchActivity : AppCompatActivity(), TrackListAdapter.OnTrackClickListener {

    private lateinit var viewModel: SearchViewModel

    override fun onTrackClick(item: Track) {
        if (viewModel.clickDebounce()) {
            try {
                viewModel.saveTrack(item)
                val playerIntent = Intent(this, PlayerActivity::class.java).apply {
                    putExtra("item", item)
                }
                startActivity(playerIntent)
            } catch (e: NullPointerException) {
                Toast.makeText(this, R.string.preview_not_found, Toast.LENGTH_SHORT).show()
            }

        }
    }

    var textInSearch: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel =
            ViewModelProvider(this, SearchViewModelFactory(this)).get(SearchViewModel::class.java)

        val returnArrow = findViewById<ImageView>(R.id.arrow_back)
        returnArrow.setOnClickListener {
            finish()
        }

        val inputSearchText = findViewById<EditText>(R.id.inputSearch)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val trackListAdapter = TrackListAdapter(this)
        val historyAdapter = TrackListAdapter(this)
        val sharedPrefs = getSharedPreferences(TRACK_HISTORY, MODE_PRIVATE)
        val searchHistory = SearchHistoryImpl(sharedPrefs)
        val clearHistory = findViewById<Button>(R.id.clear_history)
        val searchHistoryText = findViewById<TextView>(R.id.search_history_text)
        val rvTrackList = findViewById<RecyclerView>(R.id.rvTrackList)
        val placeholder = findViewById<LinearLayout>(R.id.search_placeholder)
        val placeholderText = findViewById<TextView>(R.id.search_placeholder_text)
        val placeholderImage = findViewById<ImageView>(R.id.search_placeholder_image)
        val placeholderButton = findViewById<Button>(R.id.search_button)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        placeholder.visibility = View.GONE
        rvTrackList.adapter = trackListAdapter
        rvTrackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val viewOfTrack = findViewById<LinearLayout>(R.id.track_list)


        fun skipHistory() {
            clearHistory.visibility = View.GONE
            searchHistoryText.visibility = View.GONE
            rvTrackList.visibility = View.GONE
        }

        fun showHistory(history: List<Track>) {
            historyAdapter.setTracks(history)
            if (historyAdapter.data.isNotEmpty()) {
                placeholder.visibility = View.GONE
                viewOfTrack.visibility = View.VISIBLE
                rvTrackList.visibility = View.VISIBLE
                clearHistory.visibility = View.VISIBLE
                searchHistoryText.visibility = View.VISIBLE
                rvTrackList.adapter = historyAdapter
            }
        }

        fun showHolder(text: Int, image: Int, button: Boolean) {
            trackListAdapter.setTracks(null)
            placeholderText.setText(text)
            placeholderImage.setImageResource(image)
            if (button) placeholderButton.visibility =
                View.VISIBLE else placeholderButton.visibility = View.GONE
            viewOfTrack.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        }


        placeholderButton.setOnClickListener {
            viewModel.findTrack(textInSearch)
        }


        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                textInSearch = s.toString()
                if (inputSearchText.hasFocus() && s?.isEmpty() == true && searchHistory.read()
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
        inputSearchText.addTextChangedListener(searchTextWatcher)

        clearHistory.setOnClickListener {
            viewModel.clearHistory()
            skipHistory()
        }

        inputSearchText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputSearchText.text.isEmpty()) showHistory(
                viewModel.showHistory()
            )
        }

        clearButton.setOnClickListener {
            inputSearchText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            trackListAdapter.setTracks(null)
        }

        fun showLoading() {
            progressBar.visibility = View.VISIBLE
            rvTrackList.visibility = View.GONE
        }

        fun clearAll() {
            viewOfTrack.visibility = View.GONE
            progressBar.visibility = View.GONE
            clearHistory.visibility = View.GONE
            searchHistoryText.visibility = View.GONE
            rvTrackList.visibility = View.GONE
            placeholder.visibility = View.GONE
        }

        fun showErrorMessage(error: NetworkError) {
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

        fun showSearchResult(tracks: List<Track>) {
            clearAll()
            trackListAdapter.setTracks(tracks)
            rvTrackList.adapter = trackListAdapter
            rvTrackList.visibility = View.VISIBLE
            viewOfTrack.visibility = View.VISIBLE
        }


        fun render(state: SearchState) {
            when (state) {
                is SearchState.SearchHistory -> {
                    showHistory(state.tracks)
                }

                is SearchState.Loading -> {
                    showLoading()
                }

                is SearchState.SearchedTracks -> {
                    skipHistory()
                    rvTrackList.adapter = trackListAdapter
                    showSearchResult(state.tracks)
                }

                is SearchState.SearchError -> {
                    showErrorMessage(state.error)
                }
            }
        }

        viewModel.observeState().observe(this) {
            render(it)
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


