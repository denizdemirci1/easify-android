package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.ArtistsDataSource
import com.easify.easify.model.Result
import com.easify.easify.model.response.ArtistsResponse

/**
 * @author: deniz.demirci
 * @date: 19.12.2020
 */

interface ArtistRepository {

  suspend fun getArtists(ids: String): Result<ArtistsResponse>
}

class ArtistRepositoryImpl(
  private val artistsDataSource: ArtistsDataSource
) : ArtistRepository {

  override suspend fun getArtists(ids: String): Result<ArtistsResponse> {
    return artistsDataSource.getArtists(ids)
  }
}
