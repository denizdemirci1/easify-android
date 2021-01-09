package com.easify.easify.ui.profile.playlists.main

import com.easify.easify.model.util.EasifyPlaylist

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

sealed class PlaylistViewEvent {

  object Authenticate : PlaylistViewEvent()

  object GetDevices: PlaylistViewEvent()

  object Play: PlaylistViewEvent()

  data class ListenIconClicked(val uri: String): PlaylistViewEvent()

  data class OpenPlaylistDetail(val playlist: EasifyPlaylist) : PlaylistViewEvent()

  data class ShowError(val message: String?) : PlaylistViewEvent()
}
