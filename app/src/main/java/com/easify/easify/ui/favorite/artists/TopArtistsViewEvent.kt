package com.easify.easify.ui.favorite.artists

/**
 * @author: deniz.demirci
 * @date: 9/26/2020
 */

sealed class TopArtistsViewEvent {

  data class ShowError(val message: String) : TopArtistsViewEvent()
}