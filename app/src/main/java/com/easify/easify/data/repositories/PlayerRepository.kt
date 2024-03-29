package com.easify.easify.data.repositories

import androidx.annotation.VisibleForTesting
import com.easify.easify.data.remote.datasource.PlayerDataSource
import com.easify.easify.model.*
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

interface PlayerRepository {

  suspend fun fetchRecentlyPlayed(before: String?): Result<HistoryResponse>

  suspend fun play(deviceId: String? = null, playObject: PlayObject)

  suspend fun getDevices(): Result<DevicesResponse>

  suspend fun getCurrentPlayback(): Result<CurrentPlaybackResponse?>
}

class PlayerRepositoryImpl @Inject constructor(
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val playerDataSource: PlayerDataSource
) : PlayerRepository {

  override suspend fun fetchRecentlyPlayed(before: String?): Result<HistoryResponse> {
    return playerDataSource.fetchRecentlyPlayed(before)
  }

  override suspend fun play(deviceId: String?, playObject: PlayObject) {
    playerDataSource.play(deviceId, playObject)
  }

  override suspend fun getDevices(): Result<DevicesResponse> {
    return playerDataSource.getDevices()
  }

  override suspend fun getCurrentPlayback(): Result<CurrentPlaybackResponse?> {
    return  playerDataSource.getCurrentPlayback()
  }
}
