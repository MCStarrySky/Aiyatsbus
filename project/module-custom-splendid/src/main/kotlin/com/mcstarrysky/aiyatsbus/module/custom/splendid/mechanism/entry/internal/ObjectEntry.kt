package com.mcstarrysky.aiyatsbus.module.custom.splendid.mechanism.entry.internal

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
}

val objString = ObjectString

val objBlock = ObjectBlock
val objEntity = ObjectEntity
val objLivingEntity = ObjectLivingEntity
val objPlayer = ObjectPlayer
val objItem = ObjectItem
val objVector = ObjectVector
val objList = ObjectList
val objLocation = ObjectLocation