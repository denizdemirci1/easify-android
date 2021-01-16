package com.easify.easify.ui.extensions

import android.content.res.Resources

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

