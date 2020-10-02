package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.*
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

interface PlayerDataSource {

  suspend fun fetchRecentlyPlayed(before: String?): Result<HistoryResponse>

  suspend fun play(deviceId: String?, playObject: PlayObject)

  suspend fun getDevices(): Result<DevicesResponse>

  suspend fun getCurrentPlayback(): Result<CurrentPlaybackResponse?>
}

class PlayerDataSourceImpl @Inject constructor(
    private val spotifyService: SpotifyService
) : PlayerDataSource {

  override suspend fun fetchRecentlyPlayed(before: String?): Result<HistoryResponse> {
    return try {
      val history = spotifyService.fetchRecentlyPlayed(before = before)
      Result.Success(history)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  override suspend fun play(deviceId: String?, playObject: PlayObject) {
    spotifyService.play(deviceId, playObject)
  }

  override suspend fun getDevices(): Result<DevicesResponse> {
    return try {
      val devices = spotifyService.getDevices()
      Result.Success(devices)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  override suspend fun getCurrentPlayback(): Result<CurrentPlaybackResponse?> {
    return try {
      val track = spotifyService.getCurrentPlayback()
      Result.Success(track)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
