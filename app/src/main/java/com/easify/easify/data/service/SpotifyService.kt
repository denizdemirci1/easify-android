package com.easify.easify.data.service

import com.easify.easify.model.*
import retrofit2.Response
import retrofit2.http.*

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

  @GET("me/top/artists")
  suspend fun fetchTopArtists(
    @Query("time_range") timeRange: String?,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int,
  ): TopArtistResponse

  @GET("me/top/tracks")
  suspend fun fetchTopTracks(
    @Query("time_range") timeRange: String?,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int,
  ): TopTrackResponse
}
