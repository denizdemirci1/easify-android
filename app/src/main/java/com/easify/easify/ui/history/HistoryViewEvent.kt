package com.easify.easify.ui.history

import com.easify.easify.model.Track

/**
 * @author: deniz.demirci
 * @date: 9/13/2020
 */

sealed class HistoryViewEvent {

  object ShowOpenSpotifyWarning: HistoryViewEvent()

  data class OnAddClicked(val track: Track) : HistoryViewEvent()

  data class ShowError(val message: String) : HistoryViewEvent()
}
