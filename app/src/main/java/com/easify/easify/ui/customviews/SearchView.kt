package com.easify.easify.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.easify.easify.R
import com.easify.easify.ui.extensions.afterTextChanged

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

private const val MIN_CHAR_LENGTH_TO_SEARCH = 2

class SearchView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

  private var search: EditText
  private var clear: ImageView

  private var hint: String? = null

  var onSearch: ((text: String) -> Unit)? = null

  init {
    inflate(context, R.layout.layout_search_view, this)

    search = findViewById(R.id.search_edit_text)
    clear = findViewById(R.id.clear_icon)

    val attributes = context.obtainStyledAttributes(attrs, R.styleable.SearchView)
    hint = attributes.getString(R.styleable.SearchView_hint)
    val hintColor = attributes.getColor(R.styleable.SearchView_hintColor, -1)
    search.hint = hint
    search.setHintTextColor(hintColor)
    initView()
    attributes.recycle()
  }

  private fun initView() {
    search.setHintTextColor(ContextCompat.getColor(context, R.color.colorSpotifyWhite))
    addTextWatcher()
    addOnFocusChangeListener()
    setListeners()
  }

  private fun setListeners() {
    clear.setOnClickListener {
      search.setText("")
      search.hint = ""
      clear.visibility = View.GONE
    }
  }

  private fun addOnFocusChangeListener() {
    search.setOnFocusChangeListener { view, hasFocus ->
      (view as? EditText)?.let { et ->
        if (hasFocus) {
          et.hint = ""
        } else {
          if (et.text.toString().isEmpty()) {
            et.hint = hint
          }
        }
      }
    }
  }

  private fun addTextWatcher() {
    search.afterTextChanged { input ->
      clear.visibility = if (input.isNotEmpty()) View.VISIBLE else View.GONE
      search.hint = if (input.isEmpty()) hint else ""
      if (input.length >= MIN_CHAR_LENGTH_TO_SEARCH) {
        onSearch?.invoke(input)
      }
    }
  }
}
