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

import com.mcstarrysky.aiyatsbus.module.kether.LiveData
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.getItemTag
import taboolib.module.nms.setItemTag

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 23:33
 */
object ActionItemTag : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("tag", "nbt")

    /**
     * item tag &item get xxx
     * */
    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        val source = reader.source().accept(reader)

        reader.mark()
        return when (reader.nextToken()) {
            "get" -> getData(reader, source)
            "set" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "path"),
                        trim("to", then = any())
                    ) { item, path, value ->
                        val tag = item.getItemTag()
                        if (value == "@REMOVE") {
                            // 删除数据
                            tag.removeDeep(path)
                        } else {
                            // 设置数据
                            tag.putDeep(path, value)
                        }
                        val newItem = item.setItemTag(tag)

                        // 将 nbt 更新后的新物品 meta 转入原物品
                        item.also { it.itemMeta = newItem.itemMeta }
                    }
                }
            }
            "remove" -> {
                reader.transfer {
                    combine(
                        source,
                        text(display = "path")
                    ) { item, path ->
                        val tag = item.getItemTag()
                        tag.removeDeep(path)
                        val newItem = item.setItemTag(tag)

                        // 将 nbt 更新后的新物品 meta 转入原物品
                        item.also { it.itemMeta = newItem.itemMeta }
                    }
                }
            }
            "has", "contains" -> {
                reader.handle {
                    combine(
                        source,
                        text(display = "path")
                    ) { item, path ->
                        item.getItemTag().getDeep(path) != null
                    }
                }
            }
            "all" -> {
                reader.handle {
                    combine(source) { item -> item.getItemTag() }
                }
            }
            else -> {
                reader.reset()
                getData(reader, source)
            }
        }
    }

    private fun getData(reader: ActionItem.Reader, source: LiveData<ItemStack>): ActionItem.Handler<out Any?> {
        return reader.handle {
            combine(
                source,
                text(display = "path"),
                optional("as", then = textOrNull()),
                optional("def", then = any())
            ) { item, path, type, def ->
                val data = item.getItemTag().getDeep(path) ?: return@combine def
                when (type) {
                    "boolean", "bool" -> data.asByte() > 0
                    "short" -> data.asShort()
                    "integer", "int" -> data.asInt()
                    "long" -> data.asLong()
                    "float" -> data.asFloat()
                    "double" -> data.asDouble()
                    "string", "str" -> data.asString()
                    else -> data.unsafeData() ?: def
                }
            }
        }
    }
}