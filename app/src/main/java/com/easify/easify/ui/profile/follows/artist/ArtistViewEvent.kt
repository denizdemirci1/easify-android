package com.easify.easify.ui.profile.follows.artist

/**
 * @author: deniz.demirci
 * @date: 29.10.2020
 */

sealed class ArtistViewEvent {

  data class ShowSnackbar(val followStatus: ArtistViewModel.FollowStatus) : ArtistViewEvent()

  data class ShowError(val message: String) : ArtistViewEvent()
}