package com.easify.easify.ui.profile.playlists.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.data.repositories.PlaylistRepository
import com.easify.easify.model.*
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

class PlaylistDetailViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val playlistRepository: PlaylistRepository,
  private val playerRepository: PlayerRepository,
  private val userManager: UserManager
) : ViewModel() {

  // region variables
  private var clickedTrack: Track? = null
  private var playlist: Playlist? = null

  private val _title = MutableLiveData<String>()
  val title: LiveData<String> = _title

  private val _isEditable = MutableLiveData(false)
  val isEditable: LiveData<Boolean> = _isEditable

  // TODO: implement a view for empty playlists
  private val _showNoTracksLayout = MutableLiveData(false)
  val showNoTracksLayout: LiveData<Boolean> = _showNoTracksLayout

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<PlaylistDetailViewEvent>>()
  val event: LiveData<Event<PlaylistDetailViewEvent>> = _event

  private fun sendEvent(event: PlaylistDetailViewEvent) {
    _event.value = Event(event)
  }
  // endregion

  fun initialize(playlist: Playlist) {
    this.playlist = playlist
    _title.value = playlist.name
    _isEditable.value = playlist.owner.id == userManager.user?.id
  }

  fun getPlaylistTracks(
    playlistId: String,
    offset: Int,
    onSuccess: (data: PlaylistTracksResponse) -> Unit
  ) {
    viewModelScope.launch {
      _loading.value = true
      playlistRepository.fetchPlaylistTracks(playlistId, offset).let { result ->
        _loading.value = false
        when (result) {
          is Result.Success -> result.data.let(onSuccess)

          is Result.Error -> {
            sendEvent(PlaylistDetailViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  fun onTrackClicked(track: Track) {
    clickedTrack = track
    viewModelScope.launch {
      if (userManager.deviceId.isNullOrEmpty()) {
        sendEvent(PlaylistDetailViewEvent.GetDevices)
      } else {
        playTrack(track)
      }
    }
  }

  fun playClickedTrack() {
    clickedTrack?.let { playTrack(it) }
  }

  private fun playTrack(track: Track) {
    viewModelScope.launch {
      playlist?.let { safePlaylist ->
        playerRepository.play(
          userManager.deviceId,
          PlayObject(context_uri = safePlaylist.uri, offset = Offset(uri = track.uri))
        )
        getCurrentPlayback(safePlaylist.uri)
      }
    }
  }

  fun onRemoveClicked(track: Track) {
    playlist?.let { safePlaylist ->
      viewModelScope.launch {
        val tracks = listOf(TracksToDelete(uri = track.uri))
        playlistRepository.removeTracksFromPlaylist(safePlaylist.id, RemoveTrackObject(tracks))
        sendEvent(PlaylistDetailViewEvent.ShowSnackbar(track.name))
        //sendEvent(PlaylistDetailViewEvent.NotifyDataChanged(ArrayList(playlistsTracksToShow)))
      }
    }
  }

  private fun getCurrentPlayback(clickedPlaylistUri: String) {
    viewModelScope.launch {
      playerRepository.getCurrentPlayback().let { result ->
        when (result) {
          is Result.Success -> {
            result.data?.context?.let { currentPlaybackContext ->
              if (currentPlaybackContext.uri.contains(clickedPlaylistUri)) {
                // TODO: playlist is playing
              }
            } ?: run { sendEvent(PlaylistDetailViewEvent.ShowOpenSpotifyWarning) }
          }

          is Result.Error -> sendEvent(PlaylistDetailViewEvent.ShowOpenSpotifyWarning)
        }
      }
    }
  }
}