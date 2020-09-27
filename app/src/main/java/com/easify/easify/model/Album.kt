package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

data class Album(
    val album_type: String,
    val external_urls: ExternalUrl,
    val id: String,
    val images: List<Image>,
    val name: String,
    val uri: String
)
