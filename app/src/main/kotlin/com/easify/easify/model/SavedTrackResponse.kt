package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 8.11.2020
 */

data class SavedTrackResponse(
  val href: String,
  val items: List<SavedTrack>,
  val limit: Int,
  val next: String,
  val offset: Int,
  val previous: String,
  val total: Int
)

data class SavedTrack(
  val added_at: String,
  val track: Track
)
