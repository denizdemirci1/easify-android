package com.easify.easify.util.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.model.Device
import com.easify.easify.model.DeviceType
import com.easify.easify.model.Result
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
) : ViewModel() {

  private val _deviceId = MutableLiveData<String?>()
  val deviceId: LiveData<String?> = _deviceId

  fun getDevices() {
    viewModelScope.launch {
      playerRepository.getDevices().let { result ->
        when (result) {
          is Result.Success -> _deviceId.value = getDeviceId(result.data.devices)
          is Result.Error -> _deviceId.value = null
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
}
