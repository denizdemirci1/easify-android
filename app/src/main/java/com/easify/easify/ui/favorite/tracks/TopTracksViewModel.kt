package com.easify.easify.ui.favorite.tracks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PersonalizationRepository
import com.easify.easify.model.Result.Success
import com.easify.easify.model.Result.Error
import com.easify.easify.model.TopTrackResponse
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/25/2020
 */

class TopTracksViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val personalizationRepository: PersonalizationRepository,
  private val userManager: UserManager
) : BaseViewModel(userManager) {

  val urisOfTracks = ArrayList<String>()

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<TopTracksViewEvent>>()
  val event: LiveData<Event<TopTracksViewEvent>> = _event

  private fun sendEvent(event: TopTracksViewEvent) {
    _event.value = Event(event)
  }

  fun getTopTracks(
    timeRange: String,
    offset: Int,
    onSuccess: (data: TopTrackResponse) -> Unit
  ) {
    viewModelScope.launch {
      _loading.value = true
      personalizationRepository.fetchTopTracks(timeRange, offset).let { result ->
        _loading.value = false
        when (result) {
          is Success -> {
            result.data.let(onSuccess)
            urisOfTracks.addAll(result.data.items.map { it.uri })
          }
          is Error -> {
            val message = parseNetworkError(result.exception, ::onAuthError)
            message?.let { sendEvent(TopTracksViewEvent.ShowError(message)) }
          }
        }
      }
    }
  }

  private fun onAuthError() {
    sendEvent(TopTracksViewEvent.Authenticate)
  }

  override fun onItemClick(item: EasifyItem, position: Int) {
    item.track?.let { track ->
      viewModelScope.launch {
        sendEvent(TopTracksViewEvent.TrackClicked(track.uri))
        if (userManager.deviceId.isNullOrEmpty()) {
          sendEvent(TopTracksViewEvent.GetDevices)
        } else {
          sendEvent(TopTracksViewEvent.Play)
        }
      }
    }
  }

  override fun onAddIconClick(item: EasifyItem) {
    item.track?.let { track ->
      sendEvent(TopTracksViewEvent.AddIconClicked(track))
    }
  }
}
