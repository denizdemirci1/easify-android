package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

data class RemoveTrackObject(
  val tracks: List<TracksToDelete>
)

data class TracksToDelete(
  val uri: String,
  val position: Int? = null
)