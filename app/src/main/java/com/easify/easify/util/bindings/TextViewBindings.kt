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
  this.text = this.context.getString(R.string.fragment_profile_follower_count, followerCount)
}
