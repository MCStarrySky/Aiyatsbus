/*
 * This file is part of Vulpecula, licensed under the MIT License.
 *
 *  Copyright (c) 2018 Bkm016
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
package com.mcstarrysky.aiyatsbus.module.kether.action.game.item

import taboolib.common.util.setSafely

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 19:14
 */
object ActionItemLore : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("lore")

    /**
     * item lore &item add &line
     * item lore &item add &line to &index
     * item flag &item add &line before/after &pattern
     * */
    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        val source = reader.source().accept(reader)

        reader.mark()
        return when (reader.nextToken()) {
            "add", "insert" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "line"),
                        optional("to", then = int()),
                        optional("before", then = text(display = "pattern")),
                        optional("after", then = text(display = "pattern")),
                    ) { item, line, index, before, after ->
                        val meta = item.itemMeta ?: return@combine item

                        val lore = meta.lore ?: mutableListOf()

                        if (lore.isEmpty()) {
                            // lore 为空，直接添加元素
                            lore += line
                        } else when {
                            index != null -> {
                                // 索引不为空
                                lore.add(index.coerceIn(0, lore.size), line)
                            }
                            before != null -> {
                                // 前置不为空
                                var cursor = lore.size
                                val regex = before.toRegex()
                                for ((i, it) in lore.withIndex()) {
                                    if (it.matches(regex)) {
                                        cursor = i
                                        break
                                    }
                                }
                                lore.add(cursor, line)
                            }
                            after != null -> {
                                // 前置不为空
                                var cursor = lore.lastIndex
                                val regex = after.toRegex()
                                for ((i, it) in lore.withIndex()) {
                                    if (it.matches(regex)) {
                                        cursor = i
                                        break
                                    }
                                }
                                lore.add(cursor + 1, line)
                            }
                            else -> {
                                // 索引未定义，直接添加元素
                                lore += line
                            }
                        }

                        // 存入数据
                        meta.lore = lore
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "modify", "set" -> {
                reader.transfer {
                    combine(
                        source,
                        int(display = "index"),
                        trim("to", then = text(display = "line"))
                    ) { item, index, line ->
                        val meta = item.itemMeta ?: return@combine item
                        val lore = meta.lore ?: mutableListOf()

                        lore.setSafely(index.coerceAtLeast(0), line, "")

                        // 存入数据
                        meta.lore = lore
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "remove", "rm" -> {
                reader.transfer {
                    combine(
                        source,
                        int(display = "index")
                    ) { item, index ->
                        val meta = item.itemMeta ?: return@combine item
                        val lore = meta.lore ?: return@combine item

                        lore.removeAt(index.coerceAtLeast(0))

                        // 存入数据
                        meta.lore = lore
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "clear" -> {
                reader.transfer {
                    combine(
                        source
                    ) { item ->
                        val meta = item.itemMeta ?: return@combine item

                        // 存入数据
                        meta.lore = null
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "reset" -> {
                reader.transfer {
                    combine(
                        source,
                        trim("to", then = multilineOrNull())
                    ) { item, lore ->
                        val meta = item.itemMeta ?: return@combine item

                        // 存入数据
                        meta.lore = lore
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            else -> {
                reader.reset()
                reader.handle {
                    combine(source) { item -> item.itemMeta?.lore }
                }
            }
        }
    }
}