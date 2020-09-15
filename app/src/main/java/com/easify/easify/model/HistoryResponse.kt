package com.easify.easify.model

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

data class HistoryResponse(
    val items: List<History>
)

data class History(
    val track: Track
)
