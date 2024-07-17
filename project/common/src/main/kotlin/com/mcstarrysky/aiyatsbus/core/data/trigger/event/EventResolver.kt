package com.mcstarrysky.aiyatsbus.core.data.trigger.event

import com.mojang.datafixers.util.Function3
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.function.BiFunction
import java.util.function.Consumer

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.data.trigger.event.EventResolver
 *
 * @author mical
 * @since 2024/7/18 00:58
 */
class EventResolver<T : Event>(
    val entityResolver: BiFunction<T, String?, LivingEntity>,
    val eventResolver: Consumer<T> = Consumer { },
    val itemResolver: Function3<T, String?, LivingEntity, ItemStack?>? = null
)