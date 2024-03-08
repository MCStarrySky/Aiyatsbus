package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal

import com.google.common.collect.HashBiMap
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry.Companion.getObject
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`.*

@Suppress("UNCHECKED_CAST")
abstract class ObjectEntry<E> : Entry() {

    fun h(obj: Any?): Pair<ObjectEntry<*>, String> {
        if (this == objString) return objString to obj.toString()
        return holderize((obj as? E) ?: return objString to obj.toString())
    }

    abstract fun holderize(obj: E): Pair<ObjectEntry<E>, String>

    fun d(holder: Any?) = disholderize(holder.toString())

    abstract fun disholderize(holder: String): E?

    open fun m(
        obj: Any?,
        cmd: String,
        params: List<String>
    ): Boolean {
        return modify((obj as? E) ?: return false, cmd, params)
    }

    open fun modify(
        obj: E,
        cmd: String,
        params: List<String>
    ) = false

    fun g(from: Any?, objName: String): Pair<ObjectEntry<*>, Any?> {
        return get(from as? E ?: return objString to null, objName)
    }

    open operator fun get(from: E, objName: String): Pair<ObjectEntry<*>, Any?> = objString to null

    companion object {

        val registeredRegistries = HashBiMap.create(
            mapOf(
                "string" to ObjectString,
                "block" to ObjectBlock,
                "entity" to ObjectEntity,
                "living_entity" to ObjectLivingEntity,
                "player" to ObjectPlayer,
                "item" to ObjectItem,
                "vector" to ObjectVector,
                "list" to ObjectList,
                "location" to ObjectLocation
            )
        )

        fun <T> getObject(name: String): ObjectEntry<T> {
            return registeredRegistries[name] as ObjectEntry<T>
        }
    }
}

internal val objString = getObject<ObjectString>("string")

internal val objBlock = getObject<ObjectBlock>("block")
internal val objEntity = getObject<ObjectEntity>("entity")
internal val objLivingEntity = getObject<ObjectLivingEntity>("living_entity")
internal val objPlayer = getObject<ObjectPlayer>("player")
internal val objItem = getObject<ObjectItem>("item")
internal val objVector = getObject<ObjectVector>("vector")
internal val objList = getObject<ObjectList>("list")
internal val objLocation = getObject<ObjectLocation>("location")