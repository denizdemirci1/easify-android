package com.easify.easify.ui.profile.playlists.detail

import com.easify.easify.model.PlaylistTrack

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

sealed class PlaylistDetailViewEvent {
  object ShowOpenSpotifyWarning: PlaylistDetailViewEvent()

  object GetDevices : PlaylistDetailViewEvent()

  data class NotifyDataChanged(val tracks: ArrayList<PlaylistTrack>) : PlaylistDetailViewEvent()

  data class ShowSnackbar(val trackName: String) : PlaylistDetailViewEvent()

  data class ShowError(val message: String) : PlaylistDetailViewEvent()
}