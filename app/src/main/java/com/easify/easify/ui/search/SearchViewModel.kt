package com.easify.easify.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.SearchRepository
import com.easify.easify.model.*
import com.easify.easify.ui.home.HomeViewEvent
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

class SearchViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val searchRepository: SearchRepository,
  private val userManager: UserManager
) : ViewModel() {

  private val tracksToShow = ArrayList<Track>()
  val artistsToShow = ArrayList<Artist>()

  private val _event = MutableLiveData<Event<HomeViewEvent>>()
  val event: LiveData<Event<HomeViewEvent>> = _event

  fun sendEvent(event: HomeViewEvent) {
    _event.value = Event(event)
  }

  fun search(type: SearchType, query: String) {
    viewModelScope.launch {
      searchRepository.search(type, query).let { result ->
        when (result) {
          is Result.Success -> handleSearchResult(type, result.data, query)
          is Result.Error -> sendEvent(HomeViewEvent.ShowError(parseNetworkError(result.exception)))
        }
      }
    }
  }

  private fun handleSearchResult(type: SearchType, data: SearchResponse, query: String) {
    when (type) {
      SearchType.TRACK -> data.tracks?.items?.let { handleRetrievedTracks(it, query) }
      SearchType.ARTIST -> data.artists?.items?.let { handleRetrievedArtist(it, query) }
    }
  }

  private fun handleRetrievedTracks(tracks: List<Track>, query: String) {
    tracksToShow.clear()
    tracksToShow.addAll(tracks)
    tracksToShow.filter { track ->
      track.name.contains(query) || track.artists[0].name.contains(query)
    }
    sendEvent(HomeViewEvent.NotifyDataChanged(ArrayList(tracksToShow)))
  }

  private fun handleRetrievedArtist(tracks: List<Artist>, query: String) {

  }

  fun onAddClicked(track: Track) {
    sendEvent(HomeViewEvent.OnAddIconClicked(track))
  }

  fun onTrackClicked(track: Track) {

  }

  fun onArtistClicked(artist: Artist) {

  }

  fun onListenIconClicked(artist: Artist) {

  }

  fun onListenIconClicked(track: Track) {
    viewModelScope.launch {
      sendEvent(HomeViewEvent.OnListenIconClicked(track.uri))
      if (userManager.deviceId.isNullOrEmpty()) {
        sendEvent(HomeViewEvent.GetDevices)
      } else {
        sendEvent(HomeViewEvent.Play)
      }
    }
  }
}
