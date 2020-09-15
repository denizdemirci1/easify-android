package com.easify.easify.util.bindings

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.easify.easify.R
import com.easify.easify.model.CurrentlyPlayingTrackResponse

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

@BindingAdapter("setFollowerCount")
fun TextView.setFollowerCount(followerCount: Int) {
  this.text = this.context.getString(R.string.fragment_profile_follower_count, followerCount)
}

@BindingAdapter(value = ["currentlyPlayingTrack", "trackId"])
fun TextView.setPlayingTrackTextColor(
  currentlyPlayingTrackResponse: CurrentlyPlayingTrackResponse?,
  trackId: String
) {
  currentlyPlayingTrackResponse?.let {
    if (it.item.id == trackId) {
      this.setTextColor(ContextCompat.getColor(this.context, R.color.colorSpotifyGreen))
    }
  }
}
