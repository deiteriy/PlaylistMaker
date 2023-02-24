package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout

class SearchActivity : AppCompatActivity() {

    var textInSearch: String = ""
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


        clearButton.setOnClickListener {
            inputSearchText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearButton.visibility = clearButtonVisibility(s)
            textInSearch = s.toString()
        }

        override fun afterTextChanged(s: Editable?) {
            // empty
        }
    }
        inputSearchText.addTextChangedListener(searchTextWatcher)
}
    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
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


}


