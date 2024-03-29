package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.Result
import com.easify.easify.model.TopArtistResponse
import com.easify.easify.model.TopTrackResponse

/**
 * @author: deniz.demirci
 * @date: 9/20/2020
 */

interface PersonalizationDataSource {
  suspend fun fetchTopArtists(timeRange: String?, offset: Int): Result<TopArtistResponse>

  suspend fun fetchTopTracks(timeRange: String?, offset: Int): Result<TopTrackResponse>
}

class PersonalizationDataSourceImpl(
  private val service: SpotifyService
) : PersonalizationDataSource {

  override suspend fun fetchTopArtists(
    timeRange: String?,
    offset: Int,
  ): Result<TopArtistResponse> {
    return try {
      val topArtist = service.fetchTopArtists(timeRange, offset = offset)
      Result.Success(topArtist)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  override suspend fun fetchTopTracks(
    timeRange: String?,
    offset: Int
  ): Result<TopTrackResponse> {
    return try {
      val topTrack = service.fetchTopTracks(timeRange = timeRange, offset = offset)
      Result.Success(topTrack)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
