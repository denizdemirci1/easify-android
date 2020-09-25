package com.easify.easify.util.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.easify.easify.R
import com.easify.easify.model.Image
import com.easify.easify.ui.extensions.loadWithPlaceHolder

/**
 * @author: deniz.demirci
 * @date: 9/13/2020
 */

@BindingAdapter("trackImage")
fun loadTrackImage(view: ImageView, images: List<Image>) {
  if (images.isNullOrEmpty()) {
    view.setImageResource(R.drawable.ic_music_note)
  } else {
    view.loadWithPlaceHolder(
      url = images[images.size -1].url,
      id = R.drawable.ic_music_note
    )
  }
}