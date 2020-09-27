package com.easify.easify.ui.favorite.tracks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PersonalizationRepository
import com.easify.easify.model.Result.Success
import com.easify.easify.model.Result.Error
import com.easify.easify.model.TopTrackResponse
import com.easify.easify.model.Track
import com.easify.easify.util.Event
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/25/2020
 */

class TopTracksViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val personalizationRepository: PersonalizationRepository
) : ViewModel() {

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<TopTracksViewEvent>>()
  val event: LiveData<Event<TopTracksViewEvent>> = _event

  private fun sendEvent(event: TopTracksViewEvent) {
    _event.value = Event(event)
  }

  fun getTopTracks(
    timeRange: String,
    offset: Int,
    onSuccess: (data: TopTrackResponse) -> Unit
  ) {
    viewModelScope.launch {
      _loading.value = true
      personalizationRepository.fetchTopTracks(timeRange, offset).let { result ->
        _loading.value = false
        when (result) {
          is Success -> result.data.let(onSuccess)
          is Error -> {
            sendEvent(TopTracksViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  fun openTrack(track: Track) {
    //TODO: Decide on what to do
  }

  fun openPlaylistsPage(track: Track) {
    //TODO: Decide on what to do
  }
}
