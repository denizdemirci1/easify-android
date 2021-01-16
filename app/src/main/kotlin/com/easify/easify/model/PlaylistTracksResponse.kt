package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

data class PlaylistTracksResponse(
  val href: String,
  val items: List<PlaylistTrack>,
  val limit: Int,
  val next: String?,
  val offset: Int,
  val previous: String?,
  val total: Int
)

data class PlaylistTrack(
  val added_at: String?,
  val added_by: AddedBy?,
  val isLocal: Boolean,
  //val primaryColor: PrimaryColor?,
  val track: Track
)

data class AddedBy(
  val external_urls: ExternalUrl,
  val href: String,
  val id: String,
  val type: String,
  val uri: String
)