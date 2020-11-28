package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

data class SearchResponse(
  val artists: SearchArtistsResponse?,
  val tracks: SearchTracksResponse?
)

data class SearchArtistsResponse(
  val href: String,
  val items: List<Artist>,
  val limit: Int,
  val next: String,
  val offset: String,
  val previous: String,
  val total: Int
)

data class SearchTracksResponse(
  val href: String,
  val items: List<Track>,
  val limit: Int,
  val next: String,
  val offset: String,
  val previous: String,
  val total: Int
)

enum class SearchType(val value: String) {
  ARTIST("artist"),
  TRACK("track")
}
