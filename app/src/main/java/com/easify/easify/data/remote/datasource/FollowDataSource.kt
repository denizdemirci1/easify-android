package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.ArtistsResponse
import com.easify.easify.model.Result
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 10/4/2020
 */

interface FollowDataSource {
  suspend fun getFollowedArtists(next: String?): Result<ArtistsResponse>
}

class FollowDataSourceImpl @Inject constructor(
  private val spotifyService: SpotifyService
): FollowDataSource {

  override suspend fun getFollowedArtists(next: String?): Result<ArtistsResponse> {
    return try {
      val artistsResponse = spotifyService.fetchFollowedArtists(next = next)
      Result.Success(artistsResponse)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}