package com.mcstarrysky.aiyatsbus.module.kether.action.item

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