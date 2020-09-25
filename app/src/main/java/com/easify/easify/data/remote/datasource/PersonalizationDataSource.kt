package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.Result
import com.easify.easify.model.TopArtist
import com.easify.easify.model.TopTrack

/**
 * @author: deniz.demirci
 * @date: 9/20/2020
 */

interface PersonalizationDataSource {
  suspend fun fetchTopArtists(timeRange: String?, limit: Int): Result<TopArtist>

  suspend fun fetchTopTracks(timeRange: String?, limit: Int): Result<TopTrack>
}

class PersonalizationDataSourceImpl(
  private val service: SpotifyService
) : PersonalizationDataSource {

  override suspend fun fetchTopArtists(
    timeRange: String?,
    limit: Int
  ): Result<TopArtist> {
    return try {
      val topArtist = service.fetchTopArtists(timeRange = timeRange, limit =  limit)
      Result.Success(topArtist)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  override suspend fun fetchTopTracks(
    timeRange: String?,
    limit: Int
  ): Result<TopTrack> {
    return try {
      val topTrack = service.fetchTopTracks(timeRange = timeRange, limit =  limit)
      Result.Success(topTrack)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}