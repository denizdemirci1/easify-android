package com.easify.easify.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

@Parcelize
data class Album(
    val album_type: String,
    val external_urls: ExternalUrl,
    val id: String,
    val images: List<Image>,
    val name: String,
    val uri: String
): Parcelable
