package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/15/2020
 */

data class CurrentlyPlayingTrackResponse(
  val item: Track,
  val isPlaying: Boolean
)