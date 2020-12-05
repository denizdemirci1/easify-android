package com.easify.easify.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

@Parcelize
data class Track(
    val album: Album,
    val artists: List<Artist>,
    val duration_ms: Int,
    val external_urls: ExternalUrl,
    val id: String,
    val name: String,
    val popularity: Int,
    val uri: String,
): Parcelable
