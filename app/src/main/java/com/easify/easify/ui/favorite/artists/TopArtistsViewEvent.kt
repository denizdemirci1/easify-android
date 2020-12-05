package com.easify.easify.ui.favorite.artists

import com.easify.easify.model.Artist

/**
 * @author: deniz.demirci
 * @date: 9/26/2020
 */

sealed class TopArtistsViewEvent {

  object GetDevices: TopArtistsViewEvent()

  object Play: TopArtistsViewEvent()

  data class ListenIconClicked(val uri: String): TopArtistsViewEvent()

  data class OpenArtistFragment(val artist: Artist) : TopArtistsViewEvent()

  data class ShowError(val message: String) : TopArtistsViewEvent()
}