package com.easify.easify.ui.home.recommendations

import com.easify.easify.model.*
import io.mockk.mockk

/**
 * @author: deniz.demirci
 * @date: 11.01.2021
 */

object MockDataProvider {

  fun provideFeatureResponse(): FeaturesResponse {
    return FeaturesResponse(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, "")
  }

  fun provideRecommendationsResponse(): RecommendationsResponse {
    val track1 = Track(
      Album("", mockk(), "", listOf(), "", ""),
      listOf(Artist(mockk(), mockk(), listOf(), "", "", listOf(), "", 0, "", "")),
      0, mockk(), "", "", 0, "1")
    val track2 = Track(
      Album("", mockk(), "", listOf(), "", ""),
      listOf(Artist(mockk(), mockk(), listOf(), "", "", listOf(), "", 0, "", "")),
      0, mockk(), "", "", 0, "2")
    return RecommendationsResponse(listOf(track1, track2))
  }

}
