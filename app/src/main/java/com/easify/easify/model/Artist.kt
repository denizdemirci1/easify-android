package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

data class Artist(
    val followers: Follower,
    val genres: List<String>,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)
