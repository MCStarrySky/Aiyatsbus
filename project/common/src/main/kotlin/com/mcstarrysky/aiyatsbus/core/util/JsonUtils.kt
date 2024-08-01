package com.mcstarrysky.aiyatsbus.core.util

import com.google.gson.Gson

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.JsonUtils
 *
 * @author mical
 * @since 2024/8/2 00:46
 */
val GSON = Gson()

fun String.isValidJson(): Boolean {
    return kotlin.runCatching {
        GSON.fromJson(this, Any::class.java)
    }.isSuccess
}