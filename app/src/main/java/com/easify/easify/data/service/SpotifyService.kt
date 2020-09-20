package com.easify.easify.data.service

import com.easify.easify.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

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

  @GET("me/player/recently-played")
  suspend fun fetchRecentlyPlayed(
      @Query("type") type: String = "track",
      @Query("limit") limit: Int = 30,
      @Query("before") before: String? = null
  ): HistoryResponse

  @PUT("me/player/play")
  suspend fun play(
    @Query("device_id") deviceId: String,
    @Body playObject: PlayObject
  ): Response<Unit>

  @GET("me/player/devices")
  suspend fun getDevices(): DevicesResponse

  @GET("me/player/currently-playing")
  suspend fun getCurrentlyPlayingTrack(): CurrentlyPlayingTrackResponse?
}
