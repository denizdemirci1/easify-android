package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.FollowDataSource
import com.easify.easify.model.FollowedArtistsResponse
import com.easify.easify.model.Result
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 10/4/2020
 */

interface FollowRepository {

  suspend fun getFollowedArtists(limit: Int?, next: String?): Result<FollowedArtistsResponse>

  suspend fun followArtist(id: String)

  suspend fun unfollowArtist(id: String)
}

class FollowRepositoryImpl @Inject constructor(
  private val followDataSource: FollowDataSource
) : FollowRepository {

  override suspend fun getFollowedArtists(limit: Int?, next: String?): Result<FollowedArtistsResponse> {
    return followDataSource.getFollowedArtists(limit = limit, next = next)
  }

  override suspend fun followArtist(id: String) {
    followDataSource.followArtist(id)
  }

  override suspend fun unfollowArtist(id: String) {
    followDataSource.unfollowArtist(id)
  }
}
