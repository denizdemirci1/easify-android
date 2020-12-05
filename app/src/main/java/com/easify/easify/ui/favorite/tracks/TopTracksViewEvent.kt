package com.easify.easify.ui.favorite.tracks

import com.easify.easify.model.Track

/**
 * @author: deniz.demirci
 * @date: 9/27/2020
 */

sealed class TopTracksViewEvent {

  object GetDevices: TopTracksViewEvent()

  object Play: TopTracksViewEvent()

  data class AddIconClicked(val track: Track) : TopTracksViewEvent()

  data class TrackClicked(val uri: String): TopTracksViewEvent()

  data class ShowError(val message: String) : TopTracksViewEvent()
}