package com.easify.easify.ui.player

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.model.*
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 10/2/2020
 */

class PlayerViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val playerRepository: PlayerRepository,
  private val userManager: UserManager
) : BaseViewModel(userManager) {

  private var uriToPlay: String? = null
  private var getCurrentPlaybackFailed = false

  private val _event = MutableLiveData<Event<PlayerViewEvent>>()
  val event: LiveData<Event<PlayerViewEvent>> = _event

  private fun sendEvent(event: PlayerViewEvent) {
    _event.value = Event(event)
  }

  fun setUriToPlay(uri: String) {
    uriToPlay = uri
  }

  fun getDevices() {
    viewModelScope.launch {
      playerRepository.getDevices().let { result ->
        when (result) {
          is Result.Success -> {
            sendEvent(PlayerViewEvent.DeviceIdSet(getDeviceId(result.data.devices)))
          }
          is Result.Error -> {
            val message = parseNetworkError(result.exception, ::onAuthError)
            message?.let { sendEvent(PlayerViewEvent.DeviceIdSet(null)) }
          }
        }
      }
    }
  }

  private fun getDeviceId(devices: List<Device>): String? {
    devices.find { device ->
      device.type == DeviceType.SMART_PHONE.type
    }?.let { device ->
      saveDeviceId(device.id)
      return device.id
    } ?: run {
      return null
    }
  }

  private fun saveDeviceId(deviceId: String) {
    userManager.deviceId = deviceId
  }

  /**
   * [playlistUri] : if track to play is clicked from a playlist, this variable is not null
   * and should be used for uri to play. Track uri should be used for offset value
   * [trackUris] : list of tracks that contain the clicked track
   */
  fun play(trackUris: List<String>? = null, playlistUri: String? = null, isTrack: Boolean = false) {
    uriToPlay?.let { uri ->
      viewModelScope.launch {
        val playObject = getPlayObject(trackUris, uri, playlistUri, isTrack)
        playerRepository.play(
          deviceId = userManager.deviceId,
          playObject = playObject
        )
        getCurrentPlayback(isTrack = isTrack)
      }
    } ?: run { sendEvent(PlayerViewEvent.ShowOpenSpotifyWarning) }
  }

  /**
   * PlayObject is created differently with respect to playable object.
   * When playable is track: PlayObject has "uris"
   * When playable is artist or playlist: PlayObject has "context_uri"
   */
  private fun getPlayObject(uris: List<String>? = null, uri: String, playlistUri: String?, isTrack: Boolean): PlayObject {
    return if (isTrack) {
      uris?.let { trackUris ->
        PlayObject(uris = trackUris, offset = Offset(uri = uri))
      } ?: PlayObject(uris = listOf(uri), offset = Offset(uri = uri))
    } else {
      playlistUri?.let { playlist ->
        PlayObject(context_uri = playlist, offset = Offset(uri = uri))
      } ?: PlayObject(context_uri = uri)
    }
  }

  /**
   * [playlistUri] : if track to play is clicked from a playlist, this variable is not null.
   * We should check if current playback uri is the uri of this playlist.
   */
  private fun getCurrentPlayback(playlistUri: String? = null, isTrack: Boolean) {
    uriToPlay?.let { uri ->
      viewModelScope.launch {
        playerRepository.getCurrentPlayback().let { result ->
          when (result) {
            is Result.Success -> {
              if (isTrack) {
                result.data?.item?.uri?.let { playingUri ->
                  if (playingUri == uri) {
                    // song is playing
                  }
                }
              } else {
                result.data?.context?.let { currentPlaybackContext ->
                  if (currentPlaybackContext.uri.contains(playlistUri ?: uri)) {
                    // Song is playing
                  }
                } ?: run { sendEvent(PlayerViewEvent.ShowOpenSpotifyWarning) }
              }
            }
            is Result.Error -> {
              when (result.exception) {
                is KotlinNullPointerException -> {
                  if (!getCurrentPlaybackFailed) {
                    getCurrentPlaybackFailed = true
                    sendEvent(PlayerViewEvent.ForceOpenSpotify(uri))
                  }
                }
                else -> {
                  val message = parseNetworkError(result.exception, ::onAuthError)
                  message?.let { sendEvent(PlayerViewEvent.ShowOpenSpotifyWarning) }
                }
              }
            }
          }
        }
      }
    } ?: run { sendEvent(PlayerViewEvent.ShowOpenSpotifyWarning) }
  }

  private fun onAuthError() {
    sendEvent(PlayerViewEvent.Authenticate)
  }
}
