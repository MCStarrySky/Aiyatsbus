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

import org.bukkit.inventory.ItemFlag

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 17:12
 */
object ActionItemFlag : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("flags", "flag")

    /**
     * item flag &item add &type
     * item flag &item remove &type
     * item flag &item clear
     * item flag &item has &type
     * */
    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        val source = reader.source().accept(reader)

        reader.mark()
        return when (reader.nextToken()) {
            "add", "plus" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "flag type")
                    ) { item, type ->
                        val meta = item.itemMeta ?: return@combine item
                        meta.addItemFlags(type.asFlag() ?: return@combine item)
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "remove", "rm" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "flag type")
                    ) { item, type ->
                        val meta = item.itemMeta ?: return@combine item
                        meta.removeItemFlags(type.asFlag() ?: return@combine item)
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
                        meta.removeItemFlags(*meta.itemFlags.toTypedArray())
                        item.also { it.itemMeta = meta }
                    }
                }
            }
            "has", "contains" -> {
                reader.handle {
                    combine(
                        source,
                        text(display = "flag type")
                    ) { item, type ->
                        val meta = item.itemMeta ?: return@combine false
                        meta.hasItemFlag(type.asFlag() ?: return@combine false)
                    }
                }
            }
            else -> {
                reader.reset()
                reader.handle {
                    combine(source) { item -> item.itemMeta?.itemFlags }
                }
            }
        }
    }

    private fun String.asFlag(): ItemFlag? {
        return ItemFlag.values().firstOrNull { it.name.equals(this, true) }
    }
}