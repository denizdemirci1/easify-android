package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.BrowseDataSource
import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.model.RecommendationsResponse
import com.easify.easify.model.Result

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

interface BrowseRepository {

  suspend fun fetchRecommendations(features: FeaturesResponse): Result<RecommendationsResponse>
}

class BrowseRepositoryImpl(
  private val browseDataSource: BrowseDataSource
) : BrowseRepository {

  override suspend fun fetchRecommendations(
    features: FeaturesResponse
  ): Result<RecommendationsResponse> {
    return browseDataSource.fetchRecommendations(features)
  }
}
