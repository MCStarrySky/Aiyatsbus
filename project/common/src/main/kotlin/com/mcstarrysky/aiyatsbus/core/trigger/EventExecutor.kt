package com.mcstarrysky.aiyatsbus.core.trigger

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import taboolib.common.platform.event.EventPriority
import taboolib.library.configuration.ConfigurationSection

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.EventExecutor
 *
 * @author mical
 * @since 2024/3/9 18:35
 */
data class EventExecutor(
    private val root: ConfigurationSection,
    val listen: String = root.getString("listen")!!,
    val handle: String = root.getString("handle") ?: "",
    val priority: EventPriority = EventPriority.valueOf(root.getString("priority") ?: "HIGHEST")
) {

    init {
        if (AiyatsbusSettings.enableKetherPreheat) {
            Aiyatsbus.api().getKetherHandler().preheat(handle)
        }
    }
}