package com.easify.easify.ui.player

/**
 * @author: deniz.demirci
 * @date: 29.10.2020
 */

sealed class PlayerViewEvent {

  object Authenticate : PlayerViewEvent()

  object ShowOpenSpotifyWarning: PlayerViewEvent()

  data class ForceOpenSpotify(val uri: String): PlayerViewEvent()

  data class DeviceIdSet(val deviceId: String?): PlayerViewEvent()
}
