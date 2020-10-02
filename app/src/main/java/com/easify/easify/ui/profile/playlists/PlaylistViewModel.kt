package com.easify.easify.ui.profile.playlists

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.data.repositories.PlaylistRepository
import com.easify.easify.model.*
import com.easify.easify.model.Result.Success
import com.easify.easify.model.Result.Error
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
  private val playerRepository: PlayerRepository,
  private val userManager: UserManager
) : ViewModel() {

  private var clickedPlaylist: Playlist? = null

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
    // TODO: open playlist detail page
  }

  fun playPlaylistClicked(playlist: Playlist) {
    clickedPlaylist = playlist
    viewModelScope.launch {
      if (userManager.deviceId.isNullOrEmpty()) {
        getDeviceId()
      } else {
        playPlaylist(playlist)
      }
    }
  }

  private fun playPlaylist(playlist: Playlist) {
    viewModelScope.launch {
      playerRepository.play(userManager.deviceId, PlayObject(context_uri = playlist.uri))
      getCurrentPlayback(playlist.uri)
    }
  }

  private fun getCurrentPlayback(clickedPlaylistUri: String) {
    viewModelScope.launch {
      playerRepository.getCurrentPlayback().let { result ->
        when (result) {
          is Success -> {
            result.data?.context?.let { currentPlaybackContext ->
              if (currentPlaybackContext.uri.contains(clickedPlaylistUri)) {
                // TODO: playlist is playing
              }
            } ?: run { sendEvent(PlaylistViewEvent.ShowOpenSpotifyWarning) }
          }

          is Error -> {
            sendEvent(PlaylistViewEvent.ShowOpenSpotifyWarning)
          }
        }
      }
    }
  }

  // region Device Id Operations
  private fun getDeviceId() {
    viewModelScope.launch {
      playerRepository.getDevices().let { result ->
        when (result) {
          is Success -> {
            saveDeviceId(result.data.devices)
          }
        }
      }
    }
  }

  /**
   * if there is a smartphone in devices, save its id
   * otherwise tell user to open spotify from smartphone
   */
  private fun saveDeviceId(devices: List<Device>) {
    devices.find { device ->
      device.type == DeviceType.SMART_PHONE.type
    }?.let { device ->
      userManager.deviceId = device.id
      clickedPlaylist?.let { playPlaylist(it) }
    } ?: run {
      sendEvent(PlaylistViewEvent.ShowOpenSpotifyWarning)
    }
  }
  //endregion
}
