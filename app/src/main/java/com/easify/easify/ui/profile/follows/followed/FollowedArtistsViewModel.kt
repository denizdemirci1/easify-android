package com.easify.easify.ui.profile.follows.followed

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.FollowRepository
import com.easify.easify.model.FollowedArtistsResponse
import com.easify.easify.model.Result.Success
import com.easify.easify.model.Result.Error
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

class FollowedArtistsViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val followRepository: FollowRepository,
  private val userManager: UserManager
) : BaseViewModel(userManager) {

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<FollowedArtistsViewEvent>>()
  val event: LiveData<Event<FollowedArtistsViewEvent>> = _event

  private fun sendEvent(event: FollowedArtistsViewEvent) {
    _event.value = Event(event)
  }

  fun getFollowedArtists(next: String?, onSuccess: (data: FollowedArtistsResponse) -> Unit) {
    viewModelScope.launch {
      _loading.value = true
      followRepository.getFollowedArtists(limit = null, next = next).let { result ->
        _loading.value = false
        when (result) {
          is Success -> result.data.let(onSuccess)
          is Error -> {
            val message = parseNetworkError(result.exception, ::onAuthError)
            message?.let { sendEvent(FollowedArtistsViewEvent.ShowError(message)) }
          }
        }
      }
    }
  }

  private fun onAuthError() {
    sendEvent(FollowedArtistsViewEvent.Authenticate)
  }

  override fun onItemClick(item: EasifyItem, position: Int) {
    item.artist?.let { artist ->
      sendEvent(FollowedArtistsViewEvent.OpenArtistFragment(artist))
    }
  }

  override fun onListenIconClick(item: EasifyItem) {
    item.artist?.let { artist ->
      viewModelScope.launch {
        sendEvent(FollowedArtistsViewEvent.ListenIconClicked(artist.uri))
        if (userManager.deviceId.isNullOrEmpty()) {
          sendEvent(FollowedArtistsViewEvent.GetDevices)
        } else {
          sendEvent(FollowedArtistsViewEvent.Play)
        }
      }
    }
  }
}
