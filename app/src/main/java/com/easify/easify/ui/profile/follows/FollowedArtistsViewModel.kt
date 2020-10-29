package com.easify.easify.ui.profile.follows

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.FollowRepository
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.model.Artist
import com.easify.easify.model.ArtistsResponse
import com.easify.easify.model.PlayObject
import com.easify.easify.model.Result.Success
import com.easify.easify.model.Result.Error
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
  private val playerRepository: PlayerRepository,
  private val userManager: UserManager
) : ViewModel() {

  private var clickedArtist: Artist? = null

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<FollowedArtistsViewEvent>>()
  val event: LiveData<Event<FollowedArtistsViewEvent>> = _event

  private fun sendEvent(event: FollowedArtistsViewEvent) {
    _event.value = Event(event)
  }

  fun getFollowedArtists(next: String?, onSuccess: (data: ArtistsResponse) -> Unit) {
    viewModelScope.launch {
      _loading.value = true
      followRepository.getFollowedArtists(limit = null, next = next).let { result ->
        _loading.value = false
        when (result) {
          is Success -> result.data.let(onSuccess)
          is Error -> {
            sendEvent(FollowedArtistsViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  fun onArtistClicked(artist: Artist) {
    sendEvent(FollowedArtistsViewEvent.OpenArtistFragment(artist))
  }

  fun onListenIconClicked(artist: Artist) {
    clickedArtist = artist
    viewModelScope.launch {
      if (userManager.deviceId.isNullOrEmpty()) {
        sendEvent(FollowedArtistsViewEvent.GetDevices)
      } else {
        playSongsOfArtist(artist)
      }
    }
  }

  fun playSongsOfClickedArtist() {
    clickedArtist?.let { playSongsOfArtist(it) }
  }

  private fun playSongsOfArtist(artist: Artist) {
    viewModelScope.launch {
      playerRepository.play(
        userManager.deviceId,
        PlayObject(context_uri = artist.uri)
      )
      getCurrentPlayback(artist.uri)
    }
  }

  private fun getCurrentPlayback(clickedArtistUri: String) {
    viewModelScope.launch {
      playerRepository.getCurrentPlayback().let { result ->
        when (result) {
          is Success -> {
            result.data?.context?.let { currentPlaybackContext ->
              if (currentPlaybackContext.uri.contains(clickedArtistUri)) {
                // TODO: artist is playing
              }
            } ?: run { sendEvent(FollowedArtistsViewEvent.ShowOpenSpotifyWarning) }
          }

          is Error -> sendEvent(FollowedArtistsViewEvent.ShowOpenSpotifyWarning)
        }
      }
    }
  }
}
