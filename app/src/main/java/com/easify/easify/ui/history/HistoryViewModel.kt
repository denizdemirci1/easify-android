package com.easify.easify.ui.history

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.model.*
import com.easify.easify.model.Result.Error
import com.easify.easify.model.Result.Success
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

class HistoryViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val playerRepository: PlayerRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _event = MutableLiveData<Event<HistoryViewEvent>>()
    val event: LiveData<Event<HistoryViewEvent>> = _event

    private val historyToShow = ArrayList<History>()
    private var clickedTrack: Track? = null

    private fun sendEvent(event: HistoryViewEvent) {
        _event.value = Event(event)
    }

    fun fetchRecentlyPlayedSongs() {
        viewModelScope.launch {
            playerRepository.fetchRecentlyPlayed().let { result ->
                when (result) {
                    is Success -> {
                        historyToShow.clear()
                        historyToShow.addAll(result.data.items.distinctBy { it.track.id })
                        sendEvent(HistoryViewEvent.NotifyDataChanged(ArrayList(historyToShow)))
                    }

                    is Error -> {
                        sendEvent(HistoryViewEvent.ShowError(parseNetworkError(result.exception)))
                    }
                }
            }
        }
    }

    private fun playTrack(track: Track) {
        viewModelScope.launch {
            playerRepository.play(
                userManager.deviceId ?: "",
                PlayObject(listOf(track.uri))
            )
            getCurrentlyPlayingTrack()
        }
    }

    private fun getCurrentlyPlayingTrack() {
        viewModelScope.launch {
            playerRepository.getCurrentlyPlayingTrack().let { result ->
                when (result) {
                    is Success -> {
                        result.data?.let { currentlyPlayingTrackResponse ->
                            if (currentlyPlayingTrackResponse.item.id == clickedTrack?.id) {
                                // TODO: song is playing. make its text green
                            }
                        } ?: run { sendEvent(HistoryViewEvent.ShowOpenSpotifyWarning) }
                    }

                    is Error -> {
                        sendEvent(HistoryViewEvent.ShowOpenSpotifyWarning)
                    }
                }
            }
        }
    }

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

    fun onTrackClicked(track: Track) {
        clickedTrack = track
        viewModelScope.launch {
            if (userManager.deviceId.isNullOrEmpty()) {
                getDeviceId()
            } else {
                playTrack(track)
            }
        }
    }

    fun onAddClicked(track: Track) {
        sendEvent(HistoryViewEvent.OnAddClicked(track))
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
            clickedTrack?.let { playTrack(it) }
        } ?: run {
            sendEvent(HistoryViewEvent.ShowOpenSpotifyWarning)
        }
    }
}
