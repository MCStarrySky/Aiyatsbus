package com.mcstarrysky.aiyatsbus.core.trigger

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import taboolib.common.platform.event.EventPriority
import taboolib.common5.Baffle
import taboolib.common5.cint
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.util.asMap
import java.util.concurrent.TimeUnit

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.EventExecutor
 *
 * @author mical
 * @since 2024/3/9 18:35
 */
data class EventExecutor(
    val listen: String,
    val handle: String,
    val priority: String?
) {

    fun getEventPriority(): EventPriority {
        return priority?.let { EventPriority.valueOf(it) } ?: EventPriority.HIGHEST
    }

    @Transient
    var baffle: Baffle? = null

    private fun preheat() {
        if (AiyatsbusSettings.enableKetherPreheat) {
            Aiyatsbus.api().getKetherHandler().preheat(handle)
        }
    }

    companion object {

        fun load(executorSection: ConfigurationSection): EventExecutor {
            val executor = Configuration.deserialize<EventExecutor>(executorSection, true).apply {
                executorSection.getConfigurationSection("baffle")?.asMap()?.let { section ->
                    baffle = when {
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
            executor.preheat()
            return executor
        }
    }
}