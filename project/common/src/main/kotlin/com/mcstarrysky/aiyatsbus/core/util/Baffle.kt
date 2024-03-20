package com.mcstarrysky.aiyatsbus.core.util

import taboolib.common5.Baffle
import taboolib.common5.cint
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.util.asMap
import java.util.concurrent.TimeUnit

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.Baffle
 *
 * @author mical
 * @since 2024/3/20 22:31
 */
fun ConfigurationSection?.baffle(): Baffle? {
    return this?.let {
        val section = it.asMap()
        when {
            "time" in section -> {
                // 按时间阻断
                val time = section["time"]?.cint ?: -1
                if (time > 0) {
                    Baffle.of(time * 50L, TimeUnit.MILLISECONDS)
                } else null
            }
            "count" in section -> {
                // 按次数阻断
                val count = section["count"]?.cint ?: -1
                if (count > 0) {
                    Baffle.of(count)
                } else null
            }
            else -> null
        }
    }
}