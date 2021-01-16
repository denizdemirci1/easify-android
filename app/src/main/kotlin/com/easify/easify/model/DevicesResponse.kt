package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/14/2020
 */

data class DevicesResponse(
  val devices: List<Device>
)

data class Device(
  val id: String,
  val is_active: Boolean,
  val is_private_session: Boolean,
  val is_restricted: Boolean,
  val name: String,
  val type: String
)

enum class DeviceType(val type: String) {
  COMPUTER("Computer"),
  TABLET("Tablet"),
  SMART_PHONE("Smartphone")
}