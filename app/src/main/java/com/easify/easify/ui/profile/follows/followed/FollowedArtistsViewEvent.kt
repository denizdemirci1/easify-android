package com.easify.easify.ui.profile.follows.followed

import com.easify.easify.model.Artist

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

sealed class FollowedArtistsViewEvent {

  object GetDevices: FollowedArtistsViewEvent()

  object Play: FollowedArtistsViewEvent()

  data class ListenIconClicked(val uri: String): FollowedArtistsViewEvent()

  data class OpenArtistFragment(val artist: Artist) : FollowedArtistsViewEvent()

  data class ShowError(val message: String) : FollowedArtistsViewEvent()
}
