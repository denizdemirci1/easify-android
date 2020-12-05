package com.easify.easify.ui.extensions

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * @author: deniz.demirci
 * @date: 9/20/2020
 */

fun View.hideKeyboard() {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Creates a fade animation for the calling view
 * @param opacity : final opacity of the view, zero by default
 * @param duration : how long time it takes to complete the animation, 500 ms for default
 */
fun View.animateFading(opacity: Float = 0f, duration: Long = 500) {
  val animator = ObjectAnimator.ofFloat(this, View.ALPHA, opacity)
  animator.duration = duration
  animator.start()
}