package com.mcstarrysky.aiyatsbus.core.data.trigger.event

import com.mcstarrysky.aiyatsbus.core.util.Function1
import com.mcstarrysky.aiyatsbus.core.util.Function3
import com.mcstarrysky.aiyatsbus.core.util.Function4
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
    val entityResolver: Function3<T, String?, LivingEntity?>,
    val eventResolver: Function1<T> = Function1 { _ -> },
    val itemResolver: Function4<T, String?, LivingEntity, ItemStack?> = Function4 { _, _, _ -> null }
)