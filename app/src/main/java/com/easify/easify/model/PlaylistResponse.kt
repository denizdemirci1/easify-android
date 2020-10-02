package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

data class PlaylistResponse(
  val href: String,
  val items: List<Playlist>,
  val limit: Int,
  val next: String,
  val offset: Int,
  val previous: String,
  val total: Int
)