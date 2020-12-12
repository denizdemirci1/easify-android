package com.easify.easify.ui.tracktoplaylist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.LibraryRepository
import com.easify.easify.data.repositories.PlaylistRepository
import com.easify.easify.model.*
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyPlaylist
import com.easify.easify.model.util.EasifyTrack
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

private const val PLAYLIST_LIKED_SONGS_POSITION = 0

/**
 * @author: deniz.demirci
 * @date: 2.11.2020
 */

class AddTrackToPlaylistViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val playlistRepository: PlaylistRepository,
  private val libraryRepository: LibraryRepository,
  private val userManager: UserManager
): BaseViewModel() {

  private lateinit var track: EasifyTrack

  /**
   * [requestCount]: To determine offset value for getPlaylistTracks request
   */
  private var requestCount = 0

  /**
   * To keep ids of clicked playlists so if it is clicked before, that means the track is already
   * there. So we assume "the track already exists" and show that snackbar
   */
  private var clickedPlaylistIds = ArrayList<String>()

  private val playlistsTracks = ArrayList<PlaylistTrack>()

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<AddTrackToPlaylistViewEvent>>()
  val event: LiveData<Event<AddTrackToPlaylistViewEvent>> = _event

  private fun sendEvent(event: AddTrackToPlaylistViewEvent) {
    _event.value = Event(event)
  }

  fun setTrackToAdd(track: EasifyTrack) {
    this.track = track
  }

  fun getPlaylists(offset: Int, onSuccess: (data: PlaylistResponse) -> Unit) {
    viewModelScope.launch {
      _loading.value = true
      playlistRepository.fetchPlaylists(offset).let { result ->
        _loading.value = false
        when (result) {
          is Result.Success -> result.data.let(onSuccess)
          is Result.Error -> {
            sendEvent(AddTrackToPlaylistViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  /**
   * Checks if the track is already in the selected playlist.
   * if it is -> returns
   * if it is not -> adds track to the playlist
   */
  private fun addTrackToPlaylist(playlist: EasifyPlaylist) {
    viewModelScope.launch {
      // if track already exist in the playlist, return
      for (playlistTrack in playlistsTracks) {
        if (playlistTrack.track.id == track.id) {
          sendEvent(AddTrackToPlaylistViewEvent.AlreadyExists(track.name, playlist.name))
          return@launch
        }
      }
      // if track doesn't exist in the playlist, add
      playlistRepository.addTrackToPlaylist(
        playlist.id,
        AddTrackObject(listOf(track.uri))
      ).let { result ->
        when (result) {
          is Result.Success -> {
            sendEvent(AddTrackToPlaylistViewEvent.TrackAdded(track.name, playlist.name))
          }
          is Result.Error -> {
            sendEvent(AddTrackToPlaylistViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  private fun getPlaylistTracks(playlist: EasifyPlaylist) {
    if (clickedPlaylistIds.contains(playlist.id)) {
      sendEvent(AddTrackToPlaylistViewEvent.AlreadyExists(track.name, playlist.name))
      return
    }
    viewModelScope.launch {
      clickedPlaylistIds.add(playlist.id)
      playlistRepository.fetchPlaylistTracks(playlist.id, requestCount * 100).let { result ->
        when (result) {
          is Result.Success -> {
            requestCount ++
            playlistsTracks.addAll(result.data.items)
            if (playlistsTracks.size < result.data.total) {
              getPlaylistTracks(playlist)
            } else {
              addTrackToPlaylist(playlist)
            }
          }
          is Result.Error -> {
            sendEvent(AddTrackToPlaylistViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  override fun onItemClick(item: EasifyItem, position: Int) {
    if (position == PLAYLIST_LIKED_SONGS_POSITION) {
      likedSongsClicked()
    } else {
      item.playlist?.let { playlist ->
        requestCount = 0
        getPlaylistTracks(playlist)
      }
    }
  }

  private fun likedSongsClicked() {
    viewModelScope.launch {
      libraryRepository.checkSavedTracks(track.id).let { result ->
        when (result) {
          is Result.Success -> {
            if (result.data[0]) {
              sendEvent(AddTrackToPlaylistViewEvent.SaveTrack(track.name, true))
            } else {
              addTrackToLikedSongs()
            }
          }
          is Result.Error -> {
            sendEvent(AddTrackToPlaylistViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  private fun addTrackToLikedSongs() {
    viewModelScope.launch {
      libraryRepository.saveTracks(track.id)
      sendEvent(AddTrackToPlaylistViewEvent.SaveTrack(track.name, false))
    }
  }

  fun getUserId(): String? = userManager.user?.id
}
