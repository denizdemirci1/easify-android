package com.easify.easify.ui.home.main

import androidx.annotation.VisibleForTesting
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.ArtistRepository
import com.easify.easify.data.repositories.FirebaseRepository
import com.easify.easify.data.repositories.TrackRepository
import com.easify.easify.model.Result
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.ui.extensions.toEasifyItemList
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 19.12.2020
 */

class HomeViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val firebaseRepository: FirebaseRepository,
  private val trackRepository: TrackRepository,
  private val artistRepository: ArtistRepository,
  val userManager: UserManager
) : BaseViewModel(userManager) {

  private val _event = MutableLiveData<Event<HomeViewEvent>>()
  val event: LiveData<Event<HomeViewEvent>> = _event

  fun sendEvent(event: HomeViewEvent) {
    _event.value = Event(event)
  }

  init {
    setFirebaseItemsReceivedListener()
  }

  /**
   * Get uri list from firebase realtime database
   */
  fun getFeaturedItems() {
    firebaseRepository.getFeaturedTracksIds()
    firebaseRepository.getFeaturedArtistsIds()
  }

  @VisibleForTesting
  fun setFirebaseItemsReceivedListener() {
    firebaseRepository.onTrackIdsReceived = { trackIds ->
      getTracks(trackIds)
    }
    firebaseRepository.onArtistIdsReceived = { artistIds ->
      getArtists(artistIds)
    }
  }

  @VisibleForTesting
  fun getTracks(trackIds: String) {
    viewModelScope.launch {
      trackRepository.getTracks(trackIds).let { result ->
        when (result) {
          is Result.Success -> {
            sendEvent(HomeViewEvent.OnFeaturedTracksReceived(result.data.tracks.toEasifyItemList()))
          }
          is Result.Error -> {
            parseNetworkError(result.exception, ::onAuthError)
            sendEvent(HomeViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  @VisibleForTesting
  fun getArtists(artistIds: String) {
    viewModelScope.launch {
      artistRepository.getArtists(artistIds).let { result ->
        when (result) {
          is Result.Success -> {
            sendEvent(HomeViewEvent.OnFeaturedArtistsReceived(result.data.artists.toEasifyItemList()))
          }
          is Result.Error -> {
            val message = parseNetworkError(result.exception, ::onAuthError)
            message?.let { sendEvent(HomeViewEvent.ShowError(message)) }
          }
        }
      }
    }
  }

  @VisibleForTesting
  fun onAuthError() {
    sendEvent(HomeViewEvent.Authenticate)
  }

  override fun onItemClick(item: EasifyItem, position: Int) {
    sendEvent(HomeViewEvent.OnItemClicked(item))
  }

  override fun onCleared() {
    firebaseRepository.removeFeaturedTracksListener()
    firebaseRepository.removeFeaturedArtistsListener()
    super.onCleared()
  }
}
