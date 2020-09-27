package com.easify.easify.ui.favorite.tracks

/**
 * @author: deniz.demirci
 * @date: 9/27/2020
 */

sealed class TopTracksViewEvent {

  data class ShowError(val message: String) : TopTracksViewEvent()
}