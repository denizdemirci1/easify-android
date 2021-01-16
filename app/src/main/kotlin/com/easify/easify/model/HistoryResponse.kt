package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

data class HistoryResponse(
    val items: List<History>,
    val next: String?,
    val cursors: Cursor?,
    val limit: Int,
    val href: String
)

/**
 * played_at: "2020-09-18T08:13:10.241Z"
 */
data class History(
    val track: Track,
    val played_at: String,
    val context: Context?
)

data class Context(
    val external_urls: ExternalUrl,
    val href: String,
    val type: String,
    val uri: String
)
