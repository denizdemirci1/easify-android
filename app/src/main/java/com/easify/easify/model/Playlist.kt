package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

data class Playlist(
  val collaborative: Boolean,
  val description: String,
  val external_urls: ExternalUrl,
  val href: String,
  val id: String,
  val images: List<Image>,
  val name: String,
  val owner: Owner,
  //val primary_color: PrimaryColor?,
  val public: Boolean,
  val snapshot_id: String,
  val tracks: Tracks,
  val type: String,
  val uri: String
)

data class Owner(
  val display_name: String,
  val external_urls: ExternalUrl,
  val href: String,
  val id: String,
  val type: String,
  val uri: String,
)

data class Tracks(
  val href: String,
  val total: Int
)