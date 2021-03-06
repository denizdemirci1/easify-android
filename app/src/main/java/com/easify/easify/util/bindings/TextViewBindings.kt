package com.easify.easify.util.bindings

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.easify.easify.R
import com.easify.easify.model.Track
import com.easify.easify.model.util.EasifyTrack

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

@BindingAdapter("setFollowerCount")
fun TextView.setFollowerCount(followerCount: Int) {
  this.text = this.context.getString(
    R.string.fragment_followed_artist_follower_count,
    String.format("%,d", followerCount)
  )
}

@BindingAdapter("setPopularity")
fun TextView.setPopularity(popularity: Int) {
  this.text = this.context.getString(R.string.fragment_followed_artist_popularity, popularity)
  // TODO: fix this
}

@BindingAdapter("setGenres")
fun TextView.setGenres(genres: List<String>) {
  var genreText = ""
  for (genre in genres) {
    genreText += "$genre,\n"
  }
  this.text = if (genreText.isEmpty()) "unknown" else genreText.substring(0, genreText.length - 2)
}

@BindingAdapter("slideText")
fun TextView.slideText(shouldSlide: Boolean) {
  this.isSelected = shouldSlide
}

@BindingAdapter("dynamicText")
fun TextView.dynamicText(track: EasifyTrack) {
  this.text = this.context.getString(
    R.string.fragment_discover_title,
    track.artistName,
    track.name
  )
}
