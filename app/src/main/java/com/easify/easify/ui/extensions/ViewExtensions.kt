package com.easify.easify.ui.extensions

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