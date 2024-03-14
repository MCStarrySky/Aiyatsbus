package com.mcstarrysky.aiyatsbus.core.trigger

import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.Trigger
 *
 * @author mical
 * @since 2024/3/9 18:36
 */
data class Trigger(private val section: ConfigurationSection?) {

    val listeners: ConcurrentHashMap<String, EventExecutor> = ConcurrentHashMap()

    init {
        loadListeners()
    }

    private fun loadListeners() {
        listeners.clear()

        section?.getConfigurationSection("listeners")?.let { listenersSection ->
            listeners += listenersSection.getKeys(false)
                .associateWith { EventExecutor.load(listenersSection.getConfigurationSection(it)!!) }
        }
    }

    fun onDisable() {
        listeners.clear()
    }
}