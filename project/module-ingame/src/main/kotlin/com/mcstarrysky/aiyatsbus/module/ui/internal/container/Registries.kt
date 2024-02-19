@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.mcstarrysky.aiyatsbus.module.ui.internal.container

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