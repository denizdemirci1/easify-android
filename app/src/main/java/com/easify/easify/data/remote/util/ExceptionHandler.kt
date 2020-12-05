package com.easify.easify.data.remote.util

import com.easify.easify.BuildConfig
import java.net.UnknownHostException
import retrofit2.HttpException

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

fun parseNetworkError(e: Exception): String {
  return when (e) {
    is HttpException -> {
      if (BuildConfig.DEBUG) {
        "Response: ${e.response()} \n Code: ${e.code()} \n Message: ${e.message()}"
      } else {
        e.response()?.errorBody()?.string() ?: "Something went wrong :("
      }
    }

    is UnknownHostException -> "It's on you, consider checking your internet connection"

    else -> "Something went wrong :("
  }
}
