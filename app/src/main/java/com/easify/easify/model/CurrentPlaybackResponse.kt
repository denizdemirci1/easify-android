package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/15/2020
 */

data class CurrentPlaybackResponse(
  val timestamp: Long,
  val context: Context?,
  val progress_ms: Int,
  val item: Track?,
  val isPlaying: Boolean
)