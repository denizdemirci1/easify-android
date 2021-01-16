package com.easify.easify.ui.favorite.artists

import com.easify.easify.model.util.EasifyArtist

/**
 * @author: deniz.demirci
 * @date: 9/26/2020
 */

sealed class TopArtistsViewEvent {

  object Authenticate : TopArtistsViewEvent()

  object GetDevices: TopArtistsViewEvent()

  object Play: TopArtistsViewEvent()

  data class ListenIconClicked(val uri: String): TopArtistsViewEvent()

  data class OpenArtistFragment(val artist: EasifyArtist) : TopArtistsViewEvent()

  data class ShowError(val message: String?) : TopArtistsViewEvent()
}