package com.mcstarrysky.aiyatsbus.core.trigger

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.util.baffle
import taboolib.common.platform.event.EventPriority
import taboolib.common5.Baffle
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration

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
                baffle = executorSection.getConfigurationSection("baffle").baffle()
            }
            executor.preheat()
            return executor
        }
    }
}