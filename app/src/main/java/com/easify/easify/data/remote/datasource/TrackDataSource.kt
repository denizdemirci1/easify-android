package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.model.Result
import com.easify.easify.model.Track
import com.easify.easify.model.response.TracksResponse

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

interface TrackDataSource {

  suspend fun fetchAudioFeatures(trackId: String): Result<FeaturesResponse>

  suspend fun getTracks(ids: String): Result<TracksResponse>
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

  override suspend fun getTracks(ids: String): Result<TracksResponse> {
    return try {
      val tracks = service.fetchTracks(ids)
      Result.Success(tracks)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
