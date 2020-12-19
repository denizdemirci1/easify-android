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
  suspend fun getFollowedArtists(limit: Int?, next: String?): Result<ArtistsResponse>

  suspend fun followArtist(id: String)

  suspend fun unfollowArtist(id: String)
}

class FollowDataSourceImpl @Inject constructor(
  private val spotifyService: SpotifyService
): FollowDataSource {

  override suspend fun getFollowedArtists(limit: Int?, next: String?): Result<ArtistsResponse> {
    return try {
      val artistsResponse = spotifyService.fetchFollowedArtists(limit = limit, next = next)
      Result.Success(artistsResponse)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  override suspend fun followArtist(id: String) {
    try {
      spotifyService.followArtist(id = id)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  override suspend fun unfollowArtist(id: String) {
    try {
      spotifyService.unfollowArtist(id = id)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
