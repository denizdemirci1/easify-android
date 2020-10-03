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
import com.easify.easify.model.Result.Success
import com.easify.easify.model.Result.Error
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

/**
 * Max & Default track count to fetch at once
 * from spotify api for a playlist
 */
private const val MAX_ITEM_COUNT = 100

class PlaylistDetailViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val playlistRepository: PlaylistRepository,
  private val playerRepository: PlayerRepository,
  private val userManager: UserManager
) : ViewModel() {

  // region variables
  private var clickedTrack: Track? = null
  private lateinit var playlist: Playlist

  private val playlistsTracksToShow = ArrayList<PlaylistTrack>()
  private var requestCount = 0

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
    getPlaylistTracks(playlist.id)
  }

  private fun getPlaylistTracks(playlistId: String) {
    viewModelScope.launch {
      _loading.value = true
      playlistRepository.fetchPlaylistTracks(
        playlistId,
        requestCount * MAX_ITEM_COUNT
      ).let { result ->
        _loading.value = false
        when (result) {
          is Success -> {
            requestCount++
            playlistsTracksToShow.addAll(result.data.items)
            if (playlistsTracksToShow.size < result.data.total) {
              getPlaylistTracks(playlistId)
            } else {
              sendEvent(PlaylistDetailViewEvent.NotifyDataChanged(ArrayList(playlistsTracksToShow)))
              if (playlistsTracksToShow.isEmpty()) {
                _showNoTracksLayout.value = true
              }
            }
          }
          is Error -> {
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
      playerRepository.play(
        userManager.deviceId,
        PlayObject(context_uri = playlist.uri, offset = Offset(uri = track.uri))
      )
      getCurrentPlayback(playlist.uri)
    }
  }

  fun removeTrack(track: Track) {
    playlistsTracksToShow.removeAll { it.track.id == track.id }
    removeTrackFromPlaylist(track)
  }

  private fun removeTrackFromPlaylist(track: Track) {
    viewModelScope.launch {
      val tracksToDelete = listOf(TracksToDelete(uri = track.uri))
      playlistRepository.removeTracksFromPlaylist(playlist.id, RemoveTrackObject(tracksToDelete))
      sendEvent(PlaylistDetailViewEvent.ShowSnackbar(track.name))
      sendEvent(PlaylistDetailViewEvent.NotifyDataChanged(ArrayList(playlistsTracksToShow)))
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
            } ?: run { sendEvent(PlaylistDetailViewEvent.ShowOpenSpotifyWarning) }
          }

          is Error -> sendEvent(PlaylistDetailViewEvent.ShowOpenSpotifyWarning)
        }
      }
    }
  }
}
