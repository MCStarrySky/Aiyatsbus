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
package com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.advance

import com.mcstarrysky.aiyatsbus.core.util.SimpleRegistry
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuItem
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import java.util.stream.Collectors
import kotlin.math.ceil

@Suppress("MemberVisibilityCanBePrivate", "unused")
data class Pageable<E>(val name: String, val size: Int, val accept: Class<E>, val getter: () -> Collection<E>) {

    private lateinit var cached: Collection<E>

    val mapped: MutableMap<Int, E> = HashMap()

    var page: Int = 0
        private set
    val maximum: Int
        get() = ceil(cached.size / size.toDouble()).toInt()

    val elements: List<E>
        get() = cached.stream()
            .skip((page * size).toLong())
            .limit(size.toLong())
            .collect(Collectors.toList())

    fun generate() {
        cached = getter()
    }

    operator fun get(slot: Int): E? = mapped[slot]

    fun hasNext(): Boolean = page < maximum

    fun hasPrevious(): Boolean = page > 0

    fun next(cycle: Boolean = false) {
        if (hasNext()) {
            page++
            return
        }
        if (cycle) {
            page = 0
        }
    }

    fun previous(cycle: Boolean = false) {
        if (hasPrevious()) {
            page--
            return
        }
        if (cycle) {
            page = maximum
        }
    }
}

fun interface PageableShaper<E> {
    fun shape(slot: Int, index: Int, item: MenuItem, element: E)
}

@Suppress("unused")
class PageConfiguration(val holder: MenuConfiguration) : SimpleRegistry<String, Pageable<*>>(HashMap()) {

    override fun getKey(value: Pageable<*>): String = value.name

    @Suppress("UNCHECKED_CAST")
    inline operator fun <reified E> invoke(name: String, keyword: String = name, shaper: PageableShaper<E>) {
        val instance = with(get(name)) {
            require(this != null) { "未定义可分页内容: $name" }
            require(accept.isAssignableFrom(E::class.java)) {
                "可分页内容 $name 的内容类型错误, 期望: ${accept.canonicalName}, 实际: ${E::class.java.canonicalName}"
            }
            this as Pageable<E>
        }
        instance.generate()

        val iterator = instance.elements.iterator()
        holder.shape.one(keyword) { slot, index, item, _ ->
            if (!iterator.hasNext()) {
                return@one
            }
            val element = iterator.next()
            instance.mapped[slot] = element
            shaper.shape(slot, index, item, element)
        }
    }

}