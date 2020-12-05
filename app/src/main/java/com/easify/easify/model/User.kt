package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

data class User(
    val country: String,
    val display_name: String,
    val email: String,
    val external_urls: ExternalUrl,
    val followers: Follower,
    val id: String,
    val images: List<Image>,
    val product: String,
    val type: String,
    val uri: String
) {
  data class ExternalUrl(
      val spotify: String
  )
}
