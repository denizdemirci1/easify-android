package com.easify.easify.ui.search

import com.easify.easify.model.Artist
import com.easify.easify.model.Track

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

sealed class SearchViewEvent {

  object GetDevices: SearchViewEvent()

  object Play: SearchViewEvent()

  data class OnTrackClicked(val track: Track): SearchViewEvent()

  data class OnArtistClicked(val artist: Artist): SearchViewEvent()

  data class OnAddIconClicked(val track: Track): SearchViewEvent()

  data class OnListenIconClicked(val uri: String): SearchViewEvent()

  data class NotifyTrackDataChanged(val trackList: ArrayList<Track>) : SearchViewEvent()

  data class NotifyArtistDataChanged(val artistList: ArrayList<Artist>) : SearchViewEvent()

  data class ShowError(val message: String) : SearchViewEvent()
}
