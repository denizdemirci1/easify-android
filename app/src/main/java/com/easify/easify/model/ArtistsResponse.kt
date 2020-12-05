package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 10/4/2020
 */

data class ArtistsResponse(
  val artists: Artists
)

data class Artists(
  val items: List<Artist>,
  val next: String?,
  val total: Int,
  val cursor: Cursor?,
  val limit: Int,
  val href: String
)