package com.easify.easify.ui.profile.follows.followed.artist

/**
 * @author: deniz.demirci
 * @date: 29.10.2020
 */

sealed class ArtistViewEvent {

  object Authenticate : ArtistViewEvent()

  data class ShowSnackbar(val followStatus: ArtistViewModel.FollowStatus) : ArtistViewEvent()

  data class ShowError(val message: String?) : ArtistViewEvent()
}