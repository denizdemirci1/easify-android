package com.easify.easify.ui.home.main

import com.easify.easify.model.util.EasifyItem

/**
 * @author: deniz.demirci
 * @date: 19.12.2020
 */

sealed class HomeViewEvent {

  data class OnItemClicked(val item: EasifyItem): HomeViewEvent()

  data class OnFeaturedTracksReceived(val tracks: ArrayList<EasifyItem>): HomeViewEvent()

  data class OnFeaturedArtistsReceived(val artists: ArrayList<EasifyItem>): HomeViewEvent()

  data class ShowError(val message: String) : HomeViewEvent()
}
