package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.PersonalizationDataSource
import com.easify.easify.model.Result
import com.easify.easify.model.TopArtistResponse
import com.easify.easify.model.TopTrackResponse

/**
 * @author: deniz.demirci
 * @date: 9/18/2020
 */

interface PersonalizationRepository {

  suspend fun fetchTopArtists(timeRange: String?, offset: Int): Result<TopArtistResponse>

  suspend fun fetchTopTracks(timeRange: String?, offset: Int): Result<TopTrackResponse>
}

class PersonalizationRepositoryImpl(
  private val personalizationDataSource: PersonalizationDataSource
) : PersonalizationRepository {
  override suspend fun fetchTopArtists(
    timeRange: String?,
    offset: Int
  ): Result<TopArtistResponse> {
    return personalizationDataSource.fetchTopArtists(timeRange, offset)
  }

  override suspend fun fetchTopTracks(
    timeRange: String?,
    offset: Int
  ): Result<TopTrackResponse> {
    return personalizationDataSource.fetchTopTracks(timeRange, offset)
  }
}
