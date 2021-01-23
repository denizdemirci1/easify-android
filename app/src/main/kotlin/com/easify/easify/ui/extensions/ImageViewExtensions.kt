package com.easify.easify.ui.extensions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.easify.easify.model.Image

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

@BindingAdapter("load")
fun ImageView.load(images: List<Image>) {
  if (images.isNullOrEmpty().not()) {
    Glide.with(this).load(images[0].url).into(this)
  }
}

fun ImageView.loadWithColorPlaceHolder(url: String?, color: Int) {
  url?.let { safeUrl ->
    Glide.with(this)
      .load(safeUrl)
      .placeholder(ColorDrawable(color))
      .into(this)
  }
}
