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

import com.mcstarrysky.aiyatsbus.core.util.dura
import com.mcstarrysky.aiyatsbus.core.util.maxDurability

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 16:32
 */
object ActionItemDurability : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("durability", "dura")

    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        val source = reader.source().accept(reader)

        reader.mark()
        return when (reader.nextToken()) {
            "add", "fix" -> {
                reader.transfer {
                    combine(
                        source,
                        trim("to", then = int(0))
                    ) { item, dura ->
                        item.also { it.dura += dura }
                    }
                }
            }
            "sub", "take", "damage", "dmg" -> {
                reader.transfer {
                    combine(
                        source,
                        trim("to", then = int(0))
                    ) { item, dura ->
                        item.also { it.dura -= dura }
                    }
                }
            }
            "set", "modify" -> {
                reader.transfer {
                    combine(
                        source,
                        trim("to", then = intOrNull())
                    ) { item, dura ->
                        item.also { it.dura = dura ?: it.dura }
                    }
                }
            }
            "current", "cur" -> {
                reader.handle {
                    combine(source) { item -> item.dura }
                }
            }
            "max" -> {
                reader.handle {
                    combine(source) { item -> item.maxDurability }
                }
            }
            else -> {
                reader.reset()
                // 默认返回耐久度
                reader.handle {
                    combine(source) { item -> item.dura }
                }
            }
        }
    }
}