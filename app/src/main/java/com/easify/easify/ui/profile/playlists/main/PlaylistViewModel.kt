package com.easify.easify.ui.profile.playlists.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.data.repositories.PlaylistRepository
import com.easify.easify.model.PlayObject
import com.easify.easify.model.Playlist
import com.easify.easify.model.PlaylistResponse
import com.easify.easify.model.Result.Error
import com.easify.easify.model.Result.Success
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

class PlaylistViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val playlistRepository: PlaylistRepository,
  private val userManager: UserManager
) : ViewModel() {

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _isListenable = MutableLiveData(true)
  val isListenable: LiveData<Boolean> = _isListenable

  private val _event = MutableLiveData<Event<PlaylistViewEvent>>()
  val event: LiveData<Event<PlaylistViewEvent>> = _event

  private fun sendEvent(event: PlaylistViewEvent) {
    _event.value = Event(event)
  }

  fun getPlaylists(offset: Int, onSuccess: (data: PlaylistResponse) -> Unit) {
    viewModelScope.launch {
      _loading.value = true
      playlistRepository.fetchPlaylists(offset).let { result ->
        _loading.value = false
        when (result) {
          is Success -> result.data.let(onSuccess)
          is Error -> {
            sendEvent(PlaylistViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  fun playlistClicked(playlist: Playlist) {
    sendEvent(PlaylistViewEvent.OpenPlaylistDetail(playlist))
  }

  fun playIconClicked(playlist: Playlist) {
    viewModelScope.launch {
      sendEvent(PlaylistViewEvent.ListenIconClicked(playlist.uri))
      if (userManager.deviceId.isNullOrEmpty()) {
        sendEvent(PlaylistViewEvent.GetDevices)
      } else {
        sendEvent(PlaylistViewEvent.Play)
      }
    }
  }
}
