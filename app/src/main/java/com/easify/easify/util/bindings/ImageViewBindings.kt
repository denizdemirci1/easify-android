package com.easify.easify.util.bindings

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.easify.easify.R
import com.easify.easify.model.Context
import com.easify.easify.model.Image
import com.easify.easify.model.util.EasifyPlaylist
import com.easify.easify.ui.extensions.loadWithPlaceHolder

/**
 * @author: deniz.demirci
 * @date: 9/13/2020
 */

@BindingAdapter("trackImage")
fun loadTrackImage(view: ImageView, images: List<Image>?) {
  if (images.isNullOrEmpty()) {
    view.setImageResource(R.drawable.ic_music_note)
  } else {
    view.loadWithPlaceHolder(
      url = images[images.size -1].url,
      id = R.drawable.ic_music_note
    )
  }
}

@BindingAdapter("trackHorizontalImage")
fun loadTrackHorizontalImage(view: ImageView, images: List<Image>?) {
  if (images.isNullOrEmpty()) {
    view.setImageResource(R.drawable.ic_music_note)
  } else {
    if (images.size > 1) {
      view.loadWithPlaceHolder(
        url = images[1].url,
        id = R.drawable.ic_music_note
      )
    } else {
      view.setImageResource(R.drawable.ic_music_note)
    }
  }
}

@BindingAdapter("artistImage")
fun loadArtistImage(view: ImageView, images: List<Image>?) {
  if (images.isNullOrEmpty()) {
    view.setImageResource(R.drawable.ic_person)
  } else {
    Glide.with(view.context)
      .load(images[images.size - 1].url)
      .placeholder(R.drawable.ic_person)
      .into(view)
  }
}

@BindingAdapter("artistImageBig")
fun loadBigArtistImage(view: ImageView, images: List<Image>?) {
  if (images.isNullOrEmpty()) {
    view.setImageResource(R.drawable.ic_person)
  } else {
    Glide.with(view.context).load(images[0].url).into(view)
  }
}

@BindingAdapter("playlistImage")
fun loadPlaylistImage(view: ImageView, playlist: EasifyPlaylist) {
  if (playlist.images.isNullOrEmpty()) {
    if (playlist.icon != null) {
      view.setImageResource(playlist.icon.foreground)
      view.background = ContextCompat.getDrawable(view.context, playlist.icon.background)
    } else {
      view.setImageResource(R.drawable.ic_playlist)
    }
  } else {
    Glide.with(view.context)
      .load(playlist.images[playlist.images.size - 1].url)
      .placeholder(R.drawable.ic_playlist)
      .into(view)
  }
}
