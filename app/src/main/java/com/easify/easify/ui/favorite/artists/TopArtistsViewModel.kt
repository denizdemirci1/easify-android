package com.easify.easify.ui.favorite.artists

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PersonalizationRepository
import com.easify.easify.model.Result.Success
import com.easify.easify.model.Result.Error
import com.easify.easify.model.TopArtistResponse
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/25/2020
 */

class TopArtistsViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val personalizationRepository: PersonalizationRepository,
  private val userManager: UserManager
) : BaseViewModel(userManager) {

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<TopArtistsViewEvent>>()
  val event: LiveData<Event<TopArtistsViewEvent>> = _event

  private fun sendEvent(event: TopArtistsViewEvent) {
    _event.value = Event(event)
  }

  fun getTopArtists(
    timeRange: String,
    offset: Int,
    onSuccess: (data: TopArtistResponse) -> Unit
  ) {
    viewModelScope.launch {
      _loading.value = true
      personalizationRepository.fetchTopArtists(timeRange, offset).let { result ->
        _loading.value = false
        when (result) {
          is Success -> result.data.let(onSuccess)
          is Error -> {
            val message = parseNetworkError(result.exception, ::onAuthError)
            message?.let { sendEvent(TopArtistsViewEvent.ShowError(message)) }
          }
        }
      }
    }
  }

  private fun onAuthError() {
    sendEvent(TopArtistsViewEvent.Authenticate)
  }

  override fun onListenIconClick(item: EasifyItem) {
    item.artist?.let { artist ->
      viewModelScope.launch {
        sendEvent(TopArtistsViewEvent.ListenIconClicked(artist.uri))
        if (userManager.deviceId.isNullOrEmpty()) {
          sendEvent(TopArtistsViewEvent.GetDevices)
        } else {
          sendEvent(TopArtistsViewEvent.Play)
        }
      }
    }
  }

  override fun onItemClick(item: EasifyItem, position: Int) {
    item.artist?.let { artist ->
      sendEvent(TopArtistsViewEvent.OpenArtistFragment(artist))
    }
  }
}