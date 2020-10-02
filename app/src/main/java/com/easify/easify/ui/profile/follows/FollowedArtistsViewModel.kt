package com.easify.easify.ui.profile.follows

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.easify.easify.util.Event

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

class FollowedArtistsViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<FollowedArtistsViewEvent>>()
  val event: LiveData<Event<FollowedArtistsViewEvent>> = _event

  private fun sendEvent(event: FollowedArtistsViewEvent) {
    _event.value = Event(event)
  }
}
