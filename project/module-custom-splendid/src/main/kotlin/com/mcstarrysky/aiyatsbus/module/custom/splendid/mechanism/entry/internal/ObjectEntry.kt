package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal

import com.google.common.collect.HashBiMap
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal.ObjectEntry.Companion.getObject
import com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.`object`.*
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

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

internal val objString = getObject<String>("string") as ObjectString

internal val objBlock = getObject<Block>("block") as ObjectBlock
internal val objEntity = getObject<Entity>("entity") as ObjectEntity
internal val objLivingEntity = getObject<LivingEntity>("living_entity") as ObjectLivingEntity
internal val objPlayer = getObject<Player>("player") as ObjectPlayer
internal val objItem = getObject<ItemStack>("item") as ObjectItem
internal val objVector = getObject<Vector>("vector") as ObjectVector
internal val objList = getObject<Pair<ObjectEntry<*>, MutableList<String>>>("list") as ObjectList
internal val objLocation = getObject<Location>("location") as ObjectLocation