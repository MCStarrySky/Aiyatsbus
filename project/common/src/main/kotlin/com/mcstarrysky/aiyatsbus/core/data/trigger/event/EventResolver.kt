package com.mcstarrysky.aiyatsbus.core.data.trigger.event

import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventResolver
 *
 * @author mical
 * @since 2024/7/18 00:58
 */
class EventResolver<in T : Event>(
    val entityResolver: (T, String?) -> LivingEntity?,
    val eventResolver: (T) -> Unit = { _ -> },
    val itemResolver: (T, String?, LivingEntity) -> ItemStack? = { _, _, _ -> null }
)