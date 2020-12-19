package com.easify.easify.ui.home.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.FirebaseRepository
import com.easify.easify.data.repositories.TrackRepository
import com.easify.easify.model.Result
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.ui.extensions.toEasifyItemList
import com.easify.easify.util.Event
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 19.12.2020
 */

class HomeViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val firebaseRepository: FirebaseRepository,
  private val trackRepository: TrackRepository
) : BaseViewModel() {

  private val _event = MutableLiveData<Event<HomeViewEvent>>()
  val event: LiveData<Event<HomeViewEvent>> = _event

  fun sendEvent(event: HomeViewEvent) {
    _event.value = Event(event)
  }

  init {
    setFeaturedTracksIdsRetrievedListener()
  }

  /**
   * Get uri list from firebase realtime database
   */
  fun getFeaturedTracksUris() {
    firebaseRepository.getFeaturedTracksIds()
  }

  private fun setFeaturedTracksIdsRetrievedListener() {
    firebaseRepository.onTrackIdListReceived = { trackIds ->
      getTracks(trackIds)
    }
  }

  private fun getTracks(trackIds: String) {
    viewModelScope.launch {
      trackRepository.getTracks(trackIds).let { result ->
        when (result) {
          is Result.Success -> {
            sendEvent(HomeViewEvent.OnFeaturedTracksReceived(result.data.tracks.toEasifyItemList()))
          }
          is Result.Error -> {
            sendEvent(HomeViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  override fun onItemClick(item: EasifyItem, position: Int) {
    sendEvent(HomeViewEvent.OnItemClicked(item))
  }
}
