package com.easify.easify.ui.extensions

import android.widget.SeekBar

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

fun SeekBar.onProgressChanged(onProgressChanged: (Float) -> Unit) {
  this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
      onProgressChanged.invoke(seekBar.progress.toFloat() / 100)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}
  })
}
