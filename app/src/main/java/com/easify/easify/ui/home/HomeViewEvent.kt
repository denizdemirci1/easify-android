package com.easify.easify.ui.home

import com.easify.easify.model.Track

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

sealed class HomeViewEvent {

  object GetDevices: HomeViewEvent()

  object Play: HomeViewEvent()

  data class OnAddIconClicked(val track: Track): HomeViewEvent()

  data class OnListenIconClicked(val uri: String): HomeViewEvent()

  data class NotifyDataChanged(val trackList: ArrayList<Track>) : HomeViewEvent()

  data class ShowError(val message: String) : HomeViewEvent()
}
