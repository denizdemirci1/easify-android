package com.easify.easify.data.remote.util

import com.easify.easify.BuildConfig
import java.net.UnknownHostException
import retrofit2.HttpException

private const val UNAUTHORIZED = 401

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

fun parseNetworkError(
  e: Exception,
  onAuthError: (() -> Unit)? = null
): String? {
  return when (e) {
    is HttpException -> {
      when (e.code()) {
        UNAUTHORIZED -> {
          onAuthError?.invoke()
          null
        }
        else -> {
          if (BuildConfig.DEBUG) {
            "Response: ${e.response()} \n Code: ${e.code()} \n Message: ${e.message()}"
          } else {
            e.response()?.errorBody()?.string() ?: "Something went wrong :("
          }
        }
      }
    }

    is UnknownHostException -> "It's on you, consider checking your internet connection"

    else -> "Something went wrong :("
  }
}
