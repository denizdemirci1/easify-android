package com.easify.easify.ui.favorite.tracks

import com.easify.easify.model.util.EasifyTrack

/**
 * @author: deniz.demirci
 * @date: 9/27/2020
 */

sealed class TopTracksViewEvent {

  object Authenticate : TopTracksViewEvent()

  object GetDevices: TopTracksViewEvent()

  object Play: TopTracksViewEvent()

  data class AddIconClicked(val track: EasifyTrack) : TopTracksViewEvent()

  data class TrackClicked(val uri: String): TopTracksViewEvent()

  data class ShowError(val message: String?) : TopTracksViewEvent()
}