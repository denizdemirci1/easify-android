package com.easify.easify.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

@Parcelize
data class Artist(
    val external_urls: ExternalUrl,
    val followers: Follower?,
    val genres: List<String>?,
    val href: String,
    val id: String,
    val images: List<Image>?,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
): Parcelable
