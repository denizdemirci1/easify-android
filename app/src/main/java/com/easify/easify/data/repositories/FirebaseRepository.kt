package com.easify.easify.data.repositories

import com.google.firebase.database.*
import javax.inject.Inject

private const val FEATURED_TRACKS = "featured_tracks"
private const val FEATURED_ARTISTS = "featured_artists"

/**
 * @author: deniz.demirci
 * @date: 19.12.2020
 */

interface FirebaseRepository {

  var onTrackIdsReceived: ((trackIds: String) -> Unit)?
  var onArtistIdsReceived: ((artistIds: String) -> Unit)?

  fun getFeaturedTracksIds()
  fun removeFeaturedTracksListener()

  fun getFeaturedArtistsIds()
  fun removeFeaturedArtistsListener()
}

class FirebaseRepositoryImpl @Inject constructor(
  private val firebaseDatabase: FirebaseDatabase
) : FirebaseRepository {

  override var onTrackIdsReceived: ((trackIds: String) -> Unit)? = null
  override var onArtistIdsReceived: ((artistIds: String) -> Unit)? = null

  private var featuredTracksRef: DatabaseReference? = null
  private var featuredTracksValueEventListener: ValueEventListener? = null

  private var featuredArtistsRef: DatabaseReference? = null
  private var featuredArtistsValueEventListener: ValueEventListener? = null

  override fun getFeaturedTracksIds() {
    featuredTracksRef = firebaseDatabase.getReference(FEATURED_TRACKS)
    featuredTracksValueEventListener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val query = getQuery(dataSnapshot)
        if (query.isNotEmpty()) {
          onTrackIdsReceived?.invoke(query)
        }
      }
      override fun onCancelled(error: DatabaseError) {}
    }
    featuredTracksRef?.addValueEventListener(featuredTracksValueEventListener!!)
  }

  override fun getFeaturedArtistsIds() {
    featuredArtistsRef = firebaseDatabase.getReference(FEATURED_ARTISTS)
    featuredArtistsValueEventListener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val query = getQuery(dataSnapshot)
        if (query.isNotEmpty()) {
          onArtistIdsReceived?.invoke(query)
        }
      }
      override fun onCancelled(error: DatabaseError) {}
    }
    featuredArtistsRef?.addValueEventListener(featuredArtistsValueEventListener!!)
  }

  override fun removeFeaturedTracksListener() {
    featuredTracksRef?.removeEventListener(featuredTracksValueEventListener!!)
  }

  override fun removeFeaturedArtistsListener() {
    featuredArtistsRef?.removeEventListener(featuredArtistsValueEventListener!!)
  }

  private fun getQuery(dataSnapshot: DataSnapshot): String {
    val ids: List<String> = dataSnapshot.children
      .filter { (it.value as String).isNotEmpty() }
      .map { it.value as String }
    var query = ""
    ids.forEach { id ->
      if (id.isNotEmpty()) {
        query += "$id,"
      }
    }
    return if (query.isNotEmpty()) query.dropLast(1) else ""
  }
}
