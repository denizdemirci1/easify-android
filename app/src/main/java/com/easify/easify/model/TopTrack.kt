package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/20/2020
 */

data class TopTrack(
  val items: List<Track>,
  val limit: Int,
  val next: String?,
  val offset: Int,
  val previous: String?,
  val total: Int
)