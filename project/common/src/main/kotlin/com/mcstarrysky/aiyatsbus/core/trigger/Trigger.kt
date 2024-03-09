package com.mcstarrysky.aiyatsbus.core.trigger

import taboolib.library.configuration.ConfigurationSection
import java.util.concurrent.ConcurrentHashMap

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.trigger.Trigger
 *
 * @author mical
 * @since 2024/3/9 18:36
 */
data class Trigger(private val section: ConfigurationSection) {

    lateinit var listeners: ConcurrentHashMap<String, EventExecutor>

    init {
        loadListeners()
    }

    fun loadListeners() {
        if (::listeners.isInitialized) listeners.clear()

        section.getConfigurationSection("listeners")?.let { listenersSection ->
            listeners = ConcurrentHashMap(listenersSection.getKeys(false)
                .associateWith { EventExecutor.load(listenersSection.getConfigurationSection(it)!!) })
        }
    }
}