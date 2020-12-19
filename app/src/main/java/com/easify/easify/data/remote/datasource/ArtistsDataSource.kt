package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.Result
import com.easify.easify.model.response.ArtistsResponse

/**
 * @author: deniz.demirci
 * @date: 19.12.2020
 */

interface ArtistsDataSource {

  suspend fun getArtists(ids: String): Result<ArtistsResponse>
}

class ArtistsDataSourceImpl(
  private val service: SpotifyService
) : ArtistsDataSource {

  override suspend fun getArtists(ids: String): Result<ArtistsResponse> {
    return try {
      val artists = service.fetchArtists(ids)
      Result.Success(artists)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
