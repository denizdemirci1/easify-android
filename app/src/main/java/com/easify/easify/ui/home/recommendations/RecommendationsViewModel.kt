package com.easify.easify.ui.home.recommendations

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.BrowseRepository
import com.easify.easify.data.repositories.PlaylistRepository
import com.easify.easify.model.*
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

class RecommendationsViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val browseRepository: BrowseRepository,
  private val playlistRepository: PlaylistRepository,
  private val userManager: UserManager
) : ViewModel() {

  private val recommendedTracks = ArrayList<Track>()

  private var playlistNameToCreate: String? = null
  private var createdPlaylist : Playlist? = null

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<RecommendationsViewEvent>>()
  val event: LiveData<Event<RecommendationsViewEvent>> = _event

  fun sendEvent(event: RecommendationsViewEvent) {
    _event.value = Event(event)
  }

  fun getRecommendations(features: FeaturesResponse) {
    viewModelScope.launch {
      _loading.value = true
      browseRepository.fetchRecommendations(features).let { result ->
        when (result) {
          is Result.Success -> {
            _loading.value = false
            recommendedTracks.clear()
            recommendedTracks.addAll(result.data.tracks)
            sendEvent(RecommendationsViewEvent.OnRecommendationsReceived(recommendedTracks))
          }

          is Result.Error -> {
            _loading.value = false
            sendEvent(RecommendationsViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  fun createPlaylist(name: String, description: String) {
    userManager.user?.id?.let { userId ->
      viewModelScope.launch {
        playlistRepository.createPlaylist(
          userId, CreatePlaylistBody(name, description)
        ).let { result ->
          when (result) {
            is Result.Success -> {
              playlistNameToCreate = name
              findPlaylist()
            }
            is Result.Error -> {
              _loading.value = false
              sendEvent(RecommendationsViewEvent.ShowError(parseNetworkError(result.exception)))
            }
          }
        }
      }
    } ?: sendEvent(RecommendationsViewEvent.ShowUserIdNotFoundError)
  }

  private fun findPlaylist() {
    viewModelScope.launch {
      playlistRepository.fetchPlaylists(0).let { result ->
        when (result) {
          is Result.Success -> {
            createdPlaylist = result.data.items.find { it.name == playlistNameToCreate }
            addTracksToThePlaylist(createdPlaylist?.id)
          }
          is Result.Error -> {
            _loading.value = false
            sendEvent(RecommendationsViewEvent.OnCreatePlaylistResponse(false))
          }
        }
      }
    }
  }

  private fun addTracksToThePlaylist(playlistId: String?) {
    playlistId?.let { id ->
      val uris = arrayListOf<String>()
      uris.addAll(recommendedTracks.map { it.uri })
      viewModelScope.launch {
        playlistRepository.addTrackToPlaylist(id, AddTrackObject(uris)).let { result ->
          when (result) {
            is Result.Success -> {
              _loading.value = false
              sendEvent(RecommendationsViewEvent.OnCreatePlaylistResponse(true))
            }
            is Result.Error -> {
              _loading.value = false
              sendEvent(RecommendationsViewEvent.OnCreatePlaylistResponse(false))
            }
          }
        }
      }
    }
  }

  fun onTrackClicked(track: Track) {
    viewModelScope.launch {
      sendEvent(RecommendationsViewEvent.TrackClicked(track.uri))
      if (userManager.deviceId.isNullOrEmpty()) {
        sendEvent(RecommendationsViewEvent.GetDevices)
      } else {
        sendEvent(RecommendationsViewEvent.Play)
      }
    }
  }

  fun onAddIconClicked(track: Track) {
    sendEvent(RecommendationsViewEvent.AddIconClicked(track))
  }
}
