package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.TrackDataSource
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.model.Result

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

interface TrackRepository {

  suspend fun fetchAudioFeatures(trackId: String): Result<FeaturesResponse>
}

class TrackRepositoryImpl(
  private val trackDataSource: TrackDataSource
) : TrackRepository {

  override suspend fun fetchAudioFeatures(trackId: String): Result<FeaturesResponse> {
    return trackDataSource.fetchAudioFeatures(trackId)
  }
}
