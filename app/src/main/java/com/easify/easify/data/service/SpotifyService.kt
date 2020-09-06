package com.easify.easify.data.service

import com.easify.easify.model.User
import retrofit2.http.GET

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

interface SpotifyService {

  companion object {
    const val BASE_URL = "https://api.spotify.com/v1/"
  }

  @GET("me")
  suspend fun fetchUser(): User
}
