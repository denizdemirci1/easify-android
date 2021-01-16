package com.easify.easify.ui.extensions

import com.easify.easify.model.Artist
import com.easify.easify.model.util.EasifyArtist
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType

/**
 * @author: deniz.demirci
 * @date: 11.12.2020
 */

fun List<Artist>.toEasifyItemList(): ArrayList<EasifyItem> {
  val easifyItems = arrayListOf<EasifyItem>()
  this.forEach { artist ->
    easifyItems.add(
      EasifyItem(
        type = EasifyItemType.ARTIST,
        artist = artist.toEasifyArtist()
      )
    )
  }
  return easifyItems
}

fun Artist.toEasifyArtist(): EasifyArtist {
  return EasifyArtist(
    id = id,
    name = name,
    follower = followers?.total,
    popularity = popularity,
    images = images,
    genres = genres,
    uri = uri
  )
}
