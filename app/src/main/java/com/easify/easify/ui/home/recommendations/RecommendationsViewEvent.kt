package com.easify.easify.ui.home.recommendations

import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyTrack

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

sealed class RecommendationsViewEvent {

  object GetDevices: RecommendationsViewEvent()

  object Play: RecommendationsViewEvent()

  data class TrackClicked(val uri: String): RecommendationsViewEvent()

  data class AddIconClicked(val track: EasifyTrack) : RecommendationsViewEvent()

  data class OnRecommendationsReceived(
    val tracks: ArrayList<EasifyItem>
  ) : RecommendationsViewEvent()

  data class OnCreatePlaylistResponse(val isSuccessful: Boolean) : RecommendationsViewEvent()

  object ShowUserIdNotFoundError: RecommendationsViewEvent()

  data class ShowError(val message: String) : RecommendationsViewEvent()
}
