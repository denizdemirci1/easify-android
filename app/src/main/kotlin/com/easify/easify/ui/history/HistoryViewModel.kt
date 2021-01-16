package com.easify.easify.ui.history

import androidx.annotation.VisibleForTesting
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.model.*
import com.easify.easify.model.Result.Error
import com.easify.easify.model.Result.Success
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

class HistoryViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val playerRepository: PlayerRepository,
  private val userManager: UserManager
) : BaseViewModel(userManager) {

  val urisOfTracks = ArrayList<String>()

  private val _event = MutableLiveData<Event<HistoryViewEvent>>()
  val event: LiveData<Event<HistoryViewEvent>> = _event

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private fun sendEvent(event: HistoryViewEvent) {
    _event.value = Event(event)
  }

  fun fetchRecentlyPlayedSongs(before: String?, onSuccess: (data: HistoryResponse) -> Unit) {
    viewModelScope.launch {
      _loading.value = true
      playerRepository.fetchRecentlyPlayed(before).let { result ->
        when (result) {
          is Success -> {
            _loading.value = false
            urisOfTracks.addAll(result.data.items.map { it.track.uri })
            result.data.let(onSuccess)
          }
          is Error -> {
            _loading.value = false
            val message = parseNetworkError(result.exception, ::onAuthError)
            message?.let { sendEvent(HistoryViewEvent.ShowError(message)) }
          }
        }
      }
    }
  }

  private fun onAuthError() {
    sendEvent(HistoryViewEvent.Authenticate)
  }

  override fun onItemClick(item: EasifyItem, position: Int) {
    item.track?.let { track ->
      viewModelScope.launch {
        sendEvent(HistoryViewEvent.TrackClicked(track.uri))
        if (userManager.deviceId.isNullOrEmpty()) {
          sendEvent(HistoryViewEvent.GetDevices)
        } else {
          sendEvent(HistoryViewEvent.Play)
        }
      }
    }
  }

  override fun onAddIconClick(item: EasifyItem) {
    item.track?.let { track ->
      sendEvent(HistoryViewEvent.AddIconClicked(track))
    }
  }
}
