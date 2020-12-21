package com.easify.easify.ui.profile.playlists.detail

import com.easify.easify.model.util.EasifyItem

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

sealed class PlaylistDetailViewEvent {

  object Authenticate : PlaylistDetailViewEvent()

  object GetDevices : PlaylistDetailViewEvent()

  object Play: PlaylistDetailViewEvent()

  data class ListenIconClicked(val uri: String): PlaylistDetailViewEvent()

  data class NotifyDataChanged(val tracks: ArrayList<EasifyItem>) : PlaylistDetailViewEvent()

  data class ShowSnackbar(val trackName: String) : PlaylistDetailViewEvent()

  data class ShowError(val message: String?) : PlaylistDetailViewEvent()
}