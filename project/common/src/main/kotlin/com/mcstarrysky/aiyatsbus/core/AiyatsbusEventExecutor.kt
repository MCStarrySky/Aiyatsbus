package com.mcstarrysky.aiyatsbus.core

import com.mcstarrysky.aiyatsbus.core.trigger.TriggerSlots
import taboolib.common.platform.event.EventPriority
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.AiyatsbusEventExecutor
 *
 * @author mical
 * @since 2024/3/10 18:15
 */
interface AiyatsbusEventExecutor {

    fun registerListeners()

    fun unregisterListeners()

    @Suppress("UNCHECKED_CAST")
    @ConfigNode(bind = "core/event-mapping.yml")
    companion object {

        @Config("core/event-mapping.yml", autoReload = true)
        lateinit var conf: Configuration
            private set

        @delegate:ConfigNode("mappings")
        val mappings: Map<String, String> by conversion<ConfigurationSection, Map<String, String>> {
            getKeys(false).associateWith { this["$it.class"].toString() }
        }

        @delegate:ConfigNode("mappings")
        val slots: Map<String, TriggerSlots> by conversion<ConfigurationSection, Map<String, TriggerSlots>> {
            getKeys(false).associateWith { TriggerSlots.valueOf(this["$it.slot"] as? String ?: return@associateWith TriggerSlots.ALL) }
        }

        @delegate:ConfigNode("mappings")
        val playerReferences: Map<String, String?> by conversion<ConfigurationSection, Map<String, String?>> {
            getKeys(false).associateWith { this["$it.playerReference"] as? String }
        }

        @delegate:ConfigNode("mappings")
        val eventPriorities: Map<String, List<EventPriority>> by conversion<ConfigurationSection, Map<String, List<EventPriority>>> {
            getKeys(false).associateWith { (this["$it.priorities"] as? List<String>)?.map(EventPriority::valueOf) ?: listOf(EventPriority.HIGHEST) }
        }
    }
}