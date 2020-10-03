package com.easify.easify.ui.profile.playlists.create

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

sealed class CreatePlaylistViewEvent {
  object Navigate: CreatePlaylistViewEvent()
  object ShowUserIdNotFoundError: CreatePlaylistViewEvent()
  data class ShowError(val message: String) : CreatePlaylistViewEvent()
}