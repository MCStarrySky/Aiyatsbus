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

import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.oneOf
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuItem
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.MenuKeyword
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuConfiguration
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.config.MenuSection

fun interface Shaper {
    fun shape(slot: Int, index: Int, item: MenuItem, keyword: MenuKeyword)
}

@Suppress("MemberVisibilityCanBePrivate", "unused")
class ShapeConfiguration(val holder: MenuConfiguration) {

    val raw: List<String> =
        holder.source.oneOf(*MenuSection.SHAPE.paths, validate = { it.isNotEmpty() }, getter = { getStringList(it) })
            ?: emptyList()
    val rows: Int = raw.size
    val range: IntRange by lazy { 0 until (rows * 9) }
    val lines: List<String> by lazy {
        raw.map {
            val length = it.length
            if (length == 9) {
                it
            } else if (length < 9) {
                it + " ".repeat(9 - length)
            } else {
                it.substring(0, 9)
            }
        }
    }
    val array: Array<String> by lazy { lines.toTypedArray() }
    val flatten: String by lazy { lines.joinToString("") }

    init {
        if (rows == 0) {
            MenuSection.SHAPE.missing()
        }
    }

    operator fun get(slot: Int): Char =
        requireNotNull(flatten.elementAtOrNull(slot)) { "尝试获取越界槽位的字符: $slot" }

    operator fun get(ref: Char, empty: Boolean = false, multi: Boolean = true): Set<Int> {
        val indexes = LinkedHashSet<Int>()
        flatten.forEachIndexed { index, char ->
            if (char == ref) {
                indexes += index
            }
        }
        if (!empty && indexes.isEmpty()) {
            MenuSection.SHAPE incorrect "未映射字符 $ref"
        }
        if (!multi && indexes.size > 1) {
            MenuSection.SHAPE incorrect "字符 $ref 映射了多个位置"
        }
        return indexes
    }

    operator fun get(keyword: String, empty: Boolean = false, multi: Boolean = true): Set<Int> {
        val indexes = LinkedHashSet<Int>()
        val ref = holder.keywords[keyword]
        if (ref != null) {
            indexes.addAll(get(ref, empty = true, multi = true))
        }

        if (!empty && indexes.isEmpty()) {
            MenuSection.SHAPE incorrect "未映射 Functional 关键词 $keyword($ref)"
        }
        if (!multi && indexes.size > 1) {
            MenuSection.SHAPE incorrect "Functional 关键词 $keyword($ref) 映射了多个位置"
        }
        return indexes
    }

    operator fun contains(slot: Int): Boolean = slot in range

    fun one(keyword: String, shaper: Shaper) {
        val template = holder.templates.require(keyword)
        this[keyword].forEachIndexed { index, slot ->
            shaper.shape(slot, index, template, MenuKeyword.of(keyword))
        }
    }

    fun all(vararg ignores: String, shaper: Shaper) {
        val repeats: MutableMap<Char, Int> = HashMap()
        flatten.forEachIndexed { slot, char ->
            val template = holder.templates.require(char)
            val keyword = holder.keywords[char]
            if (keyword != null && keyword in ignores) {
                return@forEachIndexed
            }
            shaper.shape(
                slot,
                repeats.compute(char) { _, current -> (current ?: -1) + 1 }!!,
                template,
                MenuKeyword.of(keyword)
            )
        }
    }

}