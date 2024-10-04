@file:Suppress("unused")

/*
 * This file is part of ParrotX, licensed under the MIT License.
 *
 *  Copyright (c) 2020 Legoshi
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function

import taboolib.library.configuration.ConfigurationSection
import java.util.*

fun String?.toUUID(): UUID? = if (this == null) null else runCatching { UUID.fromString(this) }.getOrNull()

inline fun <reified E> Map<*, *>.asList(node: String): List<E>? {
    return when (val obj = this[node]) {
        is E -> listOf(obj)
        is Collection<*> -> obj.filterIsInstance<E>()
        else -> null
    }
}

inline fun <reified E> ConfigurationSection.asList(node: String): List<E>? {
    return when (val obj = this[node]) {
        is E -> listOf(obj)
        is Collection<*> -> obj.filterIsInstance<E>()
        else -> null
    }
}

fun <V> ConfigurationSection.asMap(path: String = "", transfer: ConfigurationSection.(String) -> V?): Map<String, V> {
    val map: MutableMap<String, V> = HashMap()
    (if (path.isEmpty()) this else getConfigurationSection(path))?.let { root ->
        root.getKeys(false).forEach { key ->
            map[key] = runCatching {
                root.transfer(key)
            }.onFailure {
                it.printStackTrace()
            }.getOrNull() ?: return@forEach
        }
    }
    return map
}

fun <V> ConfigurationSection.oneOf(
    vararg paths: String,
    validate: (V) -> Boolean = { true },
    getter: ConfigurationSection.(String) -> V?
): V? {
    return paths.firstNotNullOfOrNull {
        getter(this, it)?.takeIf(validate)
    }
}