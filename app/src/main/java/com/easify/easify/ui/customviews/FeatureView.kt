package com.easify.easify.ui.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.easify.easify.R
import com.easify.easify.ui.extensions.onProgressChanged
import com.easify.easify.ui.home.features.data.FeatureType

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

private const val MULTIPLY_100 = 100

class FeatureView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

  private var label: TextView
  private var seekBar: SeekBar

  private var isEditable: Boolean
  private var labelStringResource: Int
  private var type: FeatureType

  init {
    inflate(context, R.layout.layout_feature_view, this)

    label = findViewById(R.id.feature_label)
    seekBar = findViewById(R.id.feature_seek_bar)

    val attributes = context.obtainStyledAttributes(attrs, R.styleable.FeatureView)
    isEditable = attributes.getBoolean(R.styleable.FeatureView_editable, false)
    labelStringResource = attributes.getResourceId(R.styleable.FeatureView_label, -1)
    type = FeatureType.values()[attributes.getInt(R.styleable.FeatureView_type, 0)]

    initView()
    attributes.recycle()
  }

  @SuppressLint("ClickableViewAccessibility")
  private fun initView() {
    if (isEditable) {
      setOnProgressChange()
    } else {
      seekBar.setOnTouchListener { _, _ -> return@setOnTouchListener true }
    }
  }

  private fun setOnProgressChange() {
    seekBar.onProgressChanged { setFeature(it) }
  }

  fun setFeature(value: Float) {
    val prettyValue = (value * MULTIPLY_100).toInt()
    label.text = context.getString(labelStringResource, prettyValue)
    seekBar.progress = prettyValue
  }

  fun getProgress() = seekBar.progress.toFloat()
}
