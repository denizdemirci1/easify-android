package com.easify.easify.ui.search

import com.easify.easify.model.util.EasifyArtist
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyTrack

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

sealed class SearchViewEvent {

  object Authenticate : SearchViewEvent()

  object GetDevices: SearchViewEvent()

  object Play: SearchViewEvent()

  data class OnTrackClicked(val track: EasifyTrack): SearchViewEvent()

  data class OnArtistClicked(val artist: EasifyArtist): SearchViewEvent()

  data class OnAddIconClicked(val track: EasifyTrack): SearchViewEvent()

  data class OnListenIconClicked(val uri: String): SearchViewEvent()

  data class NotifyTrackDataChanged(val trackList: ArrayList<EasifyItem>) : SearchViewEvent()

  data class NotifyArtistDataChanged(val artistList: ArrayList<EasifyItem>) : SearchViewEvent()

  data class ShowError(val message: String?) : SearchViewEvent()
}
