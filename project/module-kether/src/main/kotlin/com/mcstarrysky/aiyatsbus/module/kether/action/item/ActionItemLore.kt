package com.mcstarrysky.aiyatsbus.module.kether.action.item

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