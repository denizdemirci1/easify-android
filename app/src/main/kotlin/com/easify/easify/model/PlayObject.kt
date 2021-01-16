package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/14/2020
 */

data class PlayObject(
  val context_uri: String? = null,
  val uris: List<String>? = null,
  val offset: Offset? = null
)

data class Offset(
  val position: Int? = null,
  val uri: String? = null
)
