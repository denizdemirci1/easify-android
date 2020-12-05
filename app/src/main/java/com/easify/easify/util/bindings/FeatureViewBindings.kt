package com.easify.easify.util.bindings

import androidx.databinding.BindingAdapter
import com.easify.easify.ui.customviews.FeatureView

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

@BindingAdapter("setProgress")
fun FeatureView.setProgress(value: Float) {
  this.setFeature(value)
}
