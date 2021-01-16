package com.easify.easify.util.storage

import com.easify.easify.model.User

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

interface Storage {

  fun setString(key: String, value: String?)
  fun getString(key: String): String?

  fun setUser(key: String, value: User?)
  fun getUser(key: String): User?

  fun setBoolean(key: String, value: Boolean)
  fun getBoolean(key: String): Boolean

  fun setLong(key: String, value: Long)
  fun getLong(key: String): Long
}
