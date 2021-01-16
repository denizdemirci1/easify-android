package com.easify.easify.ui.extensions

import com.easify.easify.model.Track
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType
import com.easify.easify.model.util.EasifyTrack

/**
 * @author: deniz.demirci
 * @date: 12.12.2020
 */

fun List<Track>.toEasifyItemList(): ArrayList<EasifyItem> {
  val easifyItems = arrayListOf<EasifyItem>()
  this.forEach { track ->
    easifyItems.add(
      EasifyItem(
        type = EasifyItemType.TRACK,
        track = track.toEasifyTrack()
      )
    )
  }
  return easifyItems
}

fun Track.toEasifyTrack(): EasifyTrack {
  return EasifyTrack(
    id = id,
    name = name,
    artistName = artists[0].name,
    albumName = album.name,
    images = album.images,
    uri = uri
  )
}
