package com.easify.easify.util.bindings

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.easify.easify.R

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
