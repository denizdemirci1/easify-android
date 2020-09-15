package com.easify.easify.data.remote.util

import com.easify.easify.util.manager.UserManager
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

class SpotifyInterceptor @Inject constructor(
    private val userManager: UserManager
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val builder = chain.request().newBuilder()

    val token: String? = userManager.token ?: ""
    token?.let { builder.addHeader("Authorization", "Bearer $it") }

    val request = builder.build()
    return chain.proceed(request)
  }
}
