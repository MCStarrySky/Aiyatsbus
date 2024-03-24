@file:Suppress("unused")

package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function

inline fun <reified T> Map<*, *>.valueOrNull(node: String): T? {
    val value = this[node] ?: return null
    return requireNotNull(value as? T) {
        "配置项 $node 类型不正确 (要求: ${T::class.java.simpleName}, 实际: ${value::class.java.simpleName})"
    }
}

inline fun <reified T> Map<*, *>.value(node: String): T = requireNotNull(valueOrNull(node)) { "缺少配置项 $node" }

inline fun <reified T> Array<*>.argumentOrNull(index: Int): T? {
    val value = elementAtOrNull(index) ?: return null
    return requireNotNull(value as? T) {
        "参数 $index 类型不正确 (要求: ${T::class.java.simpleName}, 实际: ${value::class.java.simpleName})"
    }
}

inline fun <reified T> Array<*>.argument(index: Int): T = requireNotNull(argumentOrNull(index)) { "缺少参数 $index" }

fun <T> Iterator<T>.nextOrNull(): T? {
    return if (hasNext()) next() else null
}