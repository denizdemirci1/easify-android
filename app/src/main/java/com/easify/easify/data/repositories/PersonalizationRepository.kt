package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.PersonalizationDataSource
import com.easify.easify.model.Result
import com.easify.easify.model.TopArtistResponse
import com.easify.easify.model.TopTrack

/**
 * @author: deniz.demirci
 * @date: 9/18/2020
 */

interface PersonalizationRepository {

  suspend fun fetchTopArtists(timeRange: String?, limit: Int, offset: Int): Result<TopArtistResponse>

  suspend fun fetchTopTracks(timeRange: String?, limit: Int): Result<TopTrack>
}

class PersonalizationRepositoryImpl(
  private val personalizationDataSource: PersonalizationDataSource
) : PersonalizationRepository {
  override suspend fun fetchTopArtists(
    timeRange: String?,
    limit: Int,
    offset: Int
  ): Result<TopArtistResponse> {
    return personalizationDataSource.fetchTopArtists(timeRange, limit, offset)
  }

  override suspend fun fetchTopTracks(
    timeRange: String?,
    limit: Int
  ): Result<TopTrack> {
    return personalizationDataSource.fetchTopTracks(timeRange, limit)
  }
}
