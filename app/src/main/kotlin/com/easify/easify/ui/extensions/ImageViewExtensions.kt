package com.easify.easify.ui.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

@BindingAdapter("load")
fun ImageView.load(url: String?) {
  url?.let { safeUrl ->
    Glide.with(this).load(safeUrl).into(this)
  }
}

fun ImageView.loadWithPlaceHolder(url: String?, @DrawableRes id: Int) {
  url?.let { safeUrl ->
    Glide.with(this)
      .load(safeUrl)
      .placeholder(id)
      .into(this)
  }
}
