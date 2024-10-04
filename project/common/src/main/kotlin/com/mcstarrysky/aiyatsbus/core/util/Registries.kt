@file:Suppress("MemberVisibilityCanBePrivate", "unused", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")

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

abstract class Registry<K, V>(val registered: MutableMap<K, V>) : Map<K, V> by registered {
    open fun transformKey(key: K): K = key

    open fun register(key: K, value: V, force: Boolean = false) {
        // requireNotNull(value) { "尝试向 ${this::class.java.canonicalName} 注册空值" }
        val transformed = transformKey(key)
        require(force || transformed !in registered) { "尝试向 ${this::class.java.canonicalName} 重复注册 $key" }
        registered[transformed] = value
    }

    open fun register(key: K, force: Boolean = false, value: () -> V): Result<V> {
        return runCatching {
            value()
        }.onSuccess {
            register(key, it, force)
        }
    }

    open fun unregister(key: K): V? = registered.remove(transformKey(key))

    open fun unregisterIf(predicate: (Map.Entry<K, V>) -> Boolean): Boolean {
        val before = size
        registered.entries.filter(predicate).forEach { (key, _) ->
            registered.remove(key)
        }
        return size < before
    }

    override fun get(key: K): V? = registered[transformKey(key)]

    fun of(key: K): V? = get(key)

    fun ofNullable(key: K?): V? = if (key != null) of(key) else null

    override fun containsKey(key: K): Boolean = registered.containsKey(transformKey(key))

    fun clearRegistry() = registered.clear()
}

abstract class SimpleRegistry<K, V>(source: MutableMap<K, V>) : Registry<K, V>(source) {
    abstract fun getKey(value: V): K

    fun register(value: V, force: Boolean = false) = register(getKey(value), value, force)

    fun register(force: Boolean = false, value: () -> V): Result<V> {
        return runCatching {
            value()
        }.onSuccess {
            register(it, force)
        }
    }
}