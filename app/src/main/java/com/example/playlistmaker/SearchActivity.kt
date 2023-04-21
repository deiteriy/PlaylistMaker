package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackListAdapter.OnTrackClickListener {

    private val sharedPrefs by lazy { getSharedPreferences(TRACK_HISTORY, MODE_PRIVATE) }
    private val searchHistory by lazy { SearchHistory(sharedPrefs) }

    override fun onTrackClick(item: Track) {
        searchHistory.write(sharedPrefs, item)
        }

    var textInSearch: String = ""
    val baseUrl = "https://itunes.apple.com"

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    val appleMusicService = retrofit.create(AppleMusicTrack::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val returnArrow = findViewById<ImageView>(R.id.arrow_back)
        returnArrow.setOnClickListener {
            finish()
        }

        val linearLayout = findViewById<LinearLayout>(R.id.container)
        val inputSearchText = findViewById<EditText>(R.id.inputSearch)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val trackListAdapter = TrackListAdapter(this)
        val historyAdapter = TrackListAdapter(this)
        val sharedPrefs = getSharedPreferences(TRACK_HISTORY, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPrefs)
        val clearHistory = findViewById<Button>(R.id.clear_history)
        val searchHistoryText = findViewById<TextView>(R.id.search_history_text)
        val rvTrackList = findViewById<RecyclerView>(R.id.rvTrackList)
        val placeholder = findViewById<LinearLayout>(R.id.search_placeholder)
        val placeholderText = findViewById<TextView>(R.id.search_placeholder_text)
        val placeholderImage = findViewById<ImageView>(R.id.search_placeholder_image)
        val placeholderButton = findViewById<Button>(R.id.search_button)
        placeholder.visibility = View.GONE
        rvTrackList.adapter = trackListAdapter
        rvTrackList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val viewOfTrack = findViewById<LinearLayout>(R.id.track_list)


        fun skipHistory() {
            clearHistory.visibility = View.GONE
            searchHistoryText.visibility = View.GONE
            searchHistory.clearHistory(sharedPrefs)
            rvTrackList.adapter = trackListAdapter
        }

        fun showHistory() {
            historyAdapter.setTracks(searchHistory.read(sharedPrefs))
            if(historyAdapter.data.isNotEmpty()) {
                placeholder.visibility = View.GONE
                viewOfTrack.visibility = View.VISIBLE
                clearHistory.visibility = View.VISIBLE
                searchHistoryText.visibility = View.VISIBLE
                rvTrackList.adapter = historyAdapter
            }
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }



        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearButton.visibility = clearButtonVisibility(s)
            textInSearch = s.toString()
            if (inputSearchText.hasFocus() && s?.isEmpty() == true && searchHistory.read(sharedPrefs).isNotEmpty()) {
                showHistory()
            } else {
                skipHistory()
            }

        }

        override fun afterTextChanged(s: Editable?) {
            // empty
        }
    }
        inputSearchText.addTextChangedListener(searchTextWatcher)








        fun showHolder(text: Int, image: Int, button: Boolean) {
            trackListAdapter.setTracks(null)
            placeholderText.setText(text)
            placeholderImage.setImageResource(image)
            if(button) placeholderButton.visibility = View.VISIBLE else placeholderButton.visibility = View.GONE
            viewOfTrack.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        }

         fun search() {

             if(inputSearchText.text.isNotEmpty()) {
                 appleMusicService.search(inputSearchText.text.toString())
                     .enqueue(object : Callback<TrackResponse> {
                         override fun onResponse(
                             call: Call<TrackResponse>,
                             response: Response<TrackResponse>
                         ) {
                             when (response.code()) {
                                 200 -> {
                                     rvTrackList.adapter = trackListAdapter
                                     if (response.body()?.results?.isNotEmpty() == true) {
                                         placeholder.visibility = View.GONE
                                         viewOfTrack.visibility = View.VISIBLE
                                         trackListAdapter.setTracks(response.body()?.results!!)

                                     } else {
                                         showHolder(R.string.nothing_found,R.drawable.nothing_found, false)
                                     }

                                 }
                                 else -> showHolder(R.string.something_went_wrong, R.drawable.no_internet, true)
                             }

                         }

                         override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                             showHolder(R.string.something_went_wrong, R.drawable.no_internet, true)
                         }

                     })
             }
        }

        placeholderButton.setOnClickListener {
            search()
        }

        inputSearchText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }






        clearHistory.setOnClickListener {
            skipHistory()
        }

        inputSearchText.setOnFocusChangeListener { view, hasFocus -> if (hasFocus && inputSearchText.text.isEmpty()) showHistory() }

        clearButton.setOnClickListener {
            inputSearchText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            trackListAdapter.setTracks(null)
        }

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE,textInSearch)
        Log.i("survivor", "сохраняем текст $textInSearch")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textInSearch = savedInstanceState.getString(SEARCH_VALUE,"")
        Log.i("survivor", "извлекаем текст $textInSearch")
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
    return if (s.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
    }
}


