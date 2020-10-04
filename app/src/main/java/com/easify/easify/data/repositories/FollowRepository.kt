package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.FollowDataSource
import com.easify.easify.model.ArtistsResponse
import com.easify.easify.model.Result
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 10/4/2020
 */

interface FollowRepository {

  suspend fun getFollowedArtists(next: String?): Result<ArtistsResponse>
}

class FollowRepositoryImpl @Inject constructor(
  private val followDataSource: FollowDataSource
) : FollowRepository {

  override suspend fun getFollowedArtists(next: String?): Result<ArtistsResponse> {
    return followDataSource.getFollowedArtists(next)
  }
}