package com.mcstarrysky.aiyatsbus.core.data.trigger.event

import com.mcstarrysky.aiyatsbus.core.util.*
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
data class EventResolver<in T : Event>(
    val entityResolver: Function2To2<T, String?, LivingEntity?, Boolean>,
    val eventResolver: Function1<T> = Function1 { _ -> },
    val itemResolver: Function3To2<T, String?, LivingEntity, ItemStack?, Boolean> = Function3To2 { _, _, _ -> null to false }
)