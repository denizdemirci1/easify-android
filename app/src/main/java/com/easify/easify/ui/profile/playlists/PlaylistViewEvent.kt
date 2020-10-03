package com.easify.easify.ui.profile.playlists

import com.easify.easify.model.Playlist

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

sealed class PlaylistViewEvent {

  object ShowOpenSpotifyWarning: PlaylistViewEvent()

  object GetDevices: PlaylistViewEvent()

  data class OpenPlaylistDetail(val playlist: Playlist) : PlaylistViewEvent()

  data class ShowError(val message: String) : PlaylistViewEvent()
}
