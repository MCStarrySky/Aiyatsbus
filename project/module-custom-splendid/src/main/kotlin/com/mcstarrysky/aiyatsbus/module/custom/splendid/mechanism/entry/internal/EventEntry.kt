package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal

import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event

abstract class EventEntry<E : Event> : Entry() {

    @Suppress("UNCHECKED_CAST")
    fun m(
        event: Event,
        entity: LivingEntity,
        cmd: String,
        params: List<String>
    ) = modify(event as E, entity, cmd, params)

    abstract fun modify(
        event: E,
        entity: LivingEntity,
        cmd: String,
        params: List<String>
    ): Boolean

    @Suppress("UNCHECKED_CAST")
    fun g(event: Event, objName: String) = get(event as E, objName)

    abstract operator fun get(event: E, objName: String): Pair<ObjectEntry<*>, Any?>
}