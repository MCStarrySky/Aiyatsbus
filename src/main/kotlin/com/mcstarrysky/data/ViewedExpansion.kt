package com.mcstarrysky.data

/**
 * AiyatsbusExpansions
 * com.mcstarrysky.data.ViewedExpansion
 *
 * @author mical
 * @since 2024/3/2 16:42
 */
data class ViewedExpansion(
    val name: String,
    val version: String,
    val author: String,
    val downloadCount: Long,
    val time: Long,
    val formattedTime: String,
    val description: String,
    val fileName: String
) {

    var historyId: Int = -1
}
