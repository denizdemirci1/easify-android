package com.easify.easify.data.repositories

import com.google.firebase.database.*
import javax.inject.Inject

private const val FEATURED_TRACKS = "featured_tracks"

/**
 * @author: deniz.demirci
 * @date: 19.12.2020
 */

interface FirebaseRepository {

  var onTrackIdListReceived: ((trackIds: String) -> Unit)?

  fun getFeaturedTracksIds()

  fun removeFeaturedTracksListener()
}

class FirebaseRepositoryImpl @Inject constructor(
  private val firebaseDatabase: FirebaseDatabase
) : FirebaseRepository {

  override var onTrackIdListReceived: ((trackIds: String) -> Unit)? = null

  private var featuredTracksRef: DatabaseReference? = null
  private var featuredTracksValueEventListener: ValueEventListener? = null

  override fun getFeaturedTracksIds() {
    featuredTracksRef = firebaseDatabase.getReference(FEATURED_TRACKS)
    featuredTracksValueEventListener = object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val trackIds: List<String> = dataSnapshot.children
          .filter { (it.value as String).isNotEmpty() }
          .map { it.value as String }
        var query = ""
        trackIds.forEach { id ->
          if (id.isNotEmpty()) {
            query += "$id,"
          }
        }
        if (query.isNotEmpty()) {
          onTrackIdListReceived?.invoke(query.dropLast(1))
        }
      }

      override fun onCancelled(error: DatabaseError) {
        // Failed to read value
      }
    }
    featuredTracksRef?.addValueEventListener(featuredTracksValueEventListener!!)
  }

  override fun removeFeaturedTracksListener() {
    featuredTracksRef?.removeEventListener(featuredTracksValueEventListener!!)
  }
}
