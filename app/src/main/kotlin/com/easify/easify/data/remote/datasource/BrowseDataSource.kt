package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.model.RecommendationsResponse
import com.easify.easify.model.Result

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

const val TARGET_DANCEABILITY = "target_danceability"
const val TARGET_ENERGY = "target_energy"
const val TARGET_SPEECHINESS = "target_speechiness"
const val TARGET_ACOUSTICNESS = "target_acousticness"
const val TARGET_INSTRUMENTALNESS = "target_instrumentalness"
const val TARGET_LIVENESS = "target_liveness"
const val TARGET_VALENCE = "target_valence"
const val TARGET_TEMPO = "target_tempo"

interface BrowseDataSource {

  suspend fun fetchRecommendations(features: FeaturesResponse): Result<RecommendationsResponse>
}

class BrowseDataSourceImpl(
  private val service: SpotifyService
): BrowseDataSource {

  override suspend fun fetchRecommendations(
    features: FeaturesResponse
  ): Result<RecommendationsResponse> {
    return try {
      val recommendations = service.fetchRecommendations(
        seedTrackId = features.id,
        queries = getAsQueries(features)
      )
      Result.Success(recommendations)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  private fun getAsQueries(features: FeaturesResponse) = mapOf(
    TARGET_DANCEABILITY to features.danceability,
    TARGET_ENERGY to features.energy,
    TARGET_SPEECHINESS to features.speechiness,
    TARGET_ACOUSTICNESS to features.acousticness,
    TARGET_INSTRUMENTALNESS to features.instrumentalness,
    TARGET_LIVENESS to features.liveness,
    TARGET_VALENCE to features.valence,
    TARGET_TEMPO to features.tempo
  )
}
