package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/18/2020
 */

data class TopArtist(
  val items: ArrayList<Artist>,
  val limit: Int,
  val next: String?,
  val offset: Int,
  val previous: String?,
  val total: Int
)