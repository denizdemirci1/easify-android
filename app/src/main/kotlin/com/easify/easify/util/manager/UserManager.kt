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
    const val DEVICE_ID = "easify.deviceId"
    const val USER = "easify.user"
    const val TOKEN_REFRESHED = "easify.tokenRefreshed"
    const val IN_APP_REVIEW_LAST_SHOWN_TIME = "easify.inAppReviewLastShownTime"
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

  var deviceId: String?
    get() = storage.getString(DEVICE_ID)
    set(value) = storage.setString(DEVICE_ID, value)

  var inAppReviewLastShownTime: Long
    get() = storage.getLong(IN_APP_REVIEW_LAST_SHOWN_TIME)
    set(value) = storage.setLong(IN_APP_REVIEW_LAST_SHOWN_TIME, value)
}
