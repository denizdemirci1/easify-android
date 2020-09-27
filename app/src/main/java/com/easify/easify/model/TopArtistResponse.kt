package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/18/2020
 */

data class TopArtistResponse(
  val items: List<Artist>,
  val total: Int,
  val limit: Int,
  val offset: Int,
  val previous: String?,
  val href: String,
  val next: String?
)
