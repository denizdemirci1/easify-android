package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

data class Track(
    val album: Album,
    val artists: ArrayList<Artist>,
    val duration_ms: Int,
    val id: String,
    val name: String,
    val popularity: Int,
    val uri: String
)
