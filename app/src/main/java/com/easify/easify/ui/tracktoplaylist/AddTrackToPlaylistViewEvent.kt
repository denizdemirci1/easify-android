package com.easify.easify.ui.tracktoplaylist

/**
 * @author: deniz.demirci
 * @date: 2.11.2020
 */

sealed class AddTrackToPlaylistViewEvent {

  data class AlreadyExists(
    val trackName: String,
    val playlistName: String
  ) : AddTrackToPlaylistViewEvent()

  data class TrackAdded(
    val trackName: String,
    val playlistName: String
  ) : AddTrackToPlaylistViewEvent()

  data class SaveTrack(val trackName: String, val isExist: Boolean) : AddTrackToPlaylistViewEvent()

  data class ShowError(val message: String) : AddTrackToPlaylistViewEvent()
}
