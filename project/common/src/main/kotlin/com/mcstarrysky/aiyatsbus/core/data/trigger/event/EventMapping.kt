package com.mcstarrysky.aiyatsbus.core.data.trigger.event

import com.mcstarrysky.aiyatsbus.core.util.enumOf
import org.bukkit.inventory.EquipmentSlot
import taboolib.common.platform.event.EventPriority
import taboolib.library.configuration.ConfigurationSection

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventMapping
 *
 * @author mical
 * @since 2024/7/18 00:29
 */
data class EventMapping(
    private val root: ConfigurationSection,

    val slots: List<EquipmentSlot> = if (root.isList("slots")) root.getStringList("slots")
        .mapNotNull { it.enumOf<EquipmentSlot>() } else listOfNotNull(
        root.getString("slots").enumOf<EquipmentSlot>()
    ),

    val playerReference: String? = root.getString("playerReference") ?: root.getString("player")
    ?: root.getString("player-reference"),

    val itemReference: String? = root.getString("itemReference") ?: root.getString("item")
    ?: root.getString("item-reference"),

    val eventProperties: List<EventPriority> = if (root.isList("slots")) root.getStringList("slots")
        .mapNotNull { it.enumOf<EventPriority>() } else listOfNotNull(
        root.getString("slots").enumOf<EventPriority>()
    )
)