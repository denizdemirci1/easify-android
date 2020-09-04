package com.easify.easify.util.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

class SharedPreferencesStorage @Inject constructor(@ApplicationContext context: Context) : Storage {

  private val sharedPreferences = context.getSharedPreferences("Easify", Context.MODE_PRIVATE)

  override fun setString(key: String, value: String) {
    with(sharedPreferences.edit()) {
      putString(key, value)
      apply()
    }
  }

  override fun getString(key: String): String {
    return sharedPreferences.getString(key, "")!!
  }
}