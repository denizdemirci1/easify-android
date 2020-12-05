package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.model.Result

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

interface TrackDataSource {

  suspend fun fetchAudioFeatures(trackId: String): Result<FeaturesResponse>
}

class TrackDataSourceImpl(
  private val service: SpotifyService
) : TrackDataSource {

  override suspend fun fetchAudioFeatures(trackId: String): Result<FeaturesResponse> {
    return try {
      val features = service.fetchAudioFeatures(trackId)
      Result.Success(features)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
