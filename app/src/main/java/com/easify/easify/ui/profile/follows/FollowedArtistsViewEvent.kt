package com.easify.easify.ui.profile.follows

import com.easify.easify.model.Artist

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

sealed class FollowedArtistsViewEvent {

  object ShowOpenSpotifyWarning: FollowedArtistsViewEvent()

  object GetDevices: FollowedArtistsViewEvent()

  data class OpenArtistFragment(val artist: Artist) : FollowedArtistsViewEvent()

  data class ShowError(val message: String) : FollowedArtistsViewEvent()
}
