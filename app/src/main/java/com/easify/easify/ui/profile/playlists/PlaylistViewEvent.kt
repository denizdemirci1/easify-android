package com.easify.easify.ui.profile.playlists

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

sealed class PlaylistViewEvent {

  object ShowOpenSpotifyWarning: PlaylistViewEvent()

  object GetDevices: PlaylistViewEvent()

  data class ShowError(val message: String) : PlaylistViewEvent()
}
