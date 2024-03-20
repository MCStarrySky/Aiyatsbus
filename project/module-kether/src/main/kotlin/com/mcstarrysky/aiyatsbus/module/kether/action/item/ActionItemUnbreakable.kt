package com.mcstarrysky.aiyatsbus.module.kether.action.item

import taboolib.library.reflex.Reflex.Companion.invokeMethod

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 23:43
 */
object ActionItemUnbreakable : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("unbreakable", "unbreak")

    /**
     * item unbreakable &item to true/false
     * */
    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        return reader.transfer {
            combine(
                source(),
                trim("to", then = bool(false))
            ) { item, unbreakable ->
                val meta = item.itemMeta ?: return@combine item

                try {
                    meta.isUnbreakable = unbreakable
                } catch (ex: NoSuchMethodError) {
                    try {
                        meta.invokeMethod<Any>("spigot")!!.invokeMethod<Any>("setUnbreakable", unbreakable)
                    } catch (ignored: NoSuchMethodException) {
                        // 忽略
                    }
                }

                item.also { it.itemMeta = meta }
            }
        }
    }
}