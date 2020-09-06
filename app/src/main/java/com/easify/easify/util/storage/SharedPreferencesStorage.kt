package com.easify.easify.util.storage

import android.content.Context
import com.easify.easify.model.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

class SharedPreferencesStorage @Inject constructor(@ApplicationContext context: Context) : Storage {

  private val sharedPreferences = context.getSharedPreferences("Easify", Context.MODE_PRIVATE)
  private val gson: Gson = Gson()

  override fun setString(key: String, value: String?) {
    with(sharedPreferences.edit()) {
      putString(key, value)
      apply()
    }
  }

  override fun getString(key: String): String? {
    return if (sharedPreferences.contains(key)) {
      sharedPreferences.getString(key, "")
    } else {
      null
    }
  }

  override fun setUser(key: String, value: User?) {
    with(sharedPreferences.edit()) {
      putString(key, gson.toJson(value))
      apply()
    }
  }

  override fun getUser(key: String): User? {
    return if (sharedPreferences.contains(key)) {
      gson.fromJson(sharedPreferences.getString(key, "{}"), User::class.java)
    } else {
      null
    }
  }

  override fun setBoolean(key: String, value: Boolean) {
    with(sharedPreferences.edit()) {
      putBoolean(key, value)
      apply()
    }
  }

  override fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)
}
