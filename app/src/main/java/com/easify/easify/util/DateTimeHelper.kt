package com.easify.easify.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

object DateTimeHelper {

  fun getToday(): String {
    val today = Calendar.getInstance().time
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(today)
  }
}
