package com.easify.easify.util.manager

import com.easify.easify.model.User
import com.easify.easify.util.storage.Storage
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

@Singleton
class UserManager @Inject constructor(
  private val storage: Storage
) {

  companion object {
    const val TOKEN = "easify.token"
    const val USER = "easify.user"
    const val TOKEN_REFRESHED = "easify.tokenRefreshed"
  }

  private val gson: Gson = Gson()

  var token: String?
    get() = storage.getString(TOKEN)
    set(value) = storage.setString(TOKEN, value)

  var user: User?
    get() = storage.getUser(USER)
    set(value) = storage.setUser(USER, value)

  var tokenRefreshed: Boolean
    get() = storage.getBoolean(TOKEN_REFRESHED)
    set(value) = storage.setBoolean(TOKEN_REFRESHED, value)
}
