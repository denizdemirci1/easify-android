package com.easify.easify.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

const val SECOND: Long = 1000L
const val MINUTE: Long = SECOND * 60
const val HOUR: Long = MINUTE * 60
const val DAY: Long = HOUR * 24
const val MONTH: Long = DAY * 31

object DateTimeHelper {

  fun getToday(): String {
    val today = Calendar.getInstance().time
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(today)
  }
}
