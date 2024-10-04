/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.core.util

import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.metadata.Metadatable
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import taboolib.platform.util.bukkitPlugin
import taboolib.platform.util.removeMeta
import taboolib.platform.util.setMeta
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.core.util.Worlds
 *
 * @author mical
 * @date 2024/8/27 17:23
 */
/**
 * 世界是否为白天
 */
val World.isDay: Boolean
    get() = time < 12300 || time > 23850

/**
 * 世界是否为黑夜
 */
val World.isNight: Boolean
    get() = time in 12301..23849

/**
 * 从 PDC 获取内容
 */
operator fun <T, Z> PersistentDataHolder.get(key: String, type: PersistentDataType<T, Z>, namespace: String = "aiyatsbus"): Z? {
    return persistentDataContainer.get(NamespacedKey(namespace, key), type)
}

/**
 * 向 PDC 设置内容
 */
operator fun <T, Z : Any> PersistentDataHolder.set(namespace: String, key: String, type: PersistentDataType<T, Z>, value: Z) {
    persistentDataContainer.set(NamespacedKey(namespace, key), type, value)
}

/**
 * 向 PDC 设置内容
 */
operator fun <T, Z : Any> PersistentDataHolder.set(key: String, type: PersistentDataType<T, Z>, value: Z) {
    set("aiyatsbus", key, type, value)
}

/**
 * 判断 PDC 是否包含某个键
 */
fun <T, Z : Any> PersistentDataHolder.has(key: String, type: PersistentDataType<T, Z>, namespace: String = "aiyatsbus"): Boolean {
    return persistentDataContainer.has(NamespacedKey(namespace, key), type)
}

/**
 * 从 PDC 移除内容
 */
fun PersistentDataHolder.remove(key: String, namespace: String = "aiyatsbus") {
    return persistentDataContainer.remove(NamespacedKey(namespace, key))
}

/**
 * 标记
 */
fun Metadatable.mark(key: String) {
    setMeta(key, bukkitPlugin)
}

fun Metadatable.unmark(key: String) {
    removeMeta(key)
}

/**
 * 将 Location 的 世界 和 XYZ 信息序列化成文本字符串
 */
val Location.serialized get() = "${world.name},$blockX,$blockY,$blockZ"