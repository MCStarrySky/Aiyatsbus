package com.mcstarrysky.aiyatsbus.module.kether.action.item

import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import java.awt.Color

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-24 15:58
 */
object ActionItemColor : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("color")

    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        return reader.transfer {
            combine(
                source(),
                trim("to", then = color())
            ) { item, color ->
                val meta = item.itemMeta ?: return@combine item

                when (meta) {
                    is LeatherArmorMeta -> meta.setColor(color.toBukkit())
                    is PotionMeta -> meta.color = color.toBukkit()
                }

                item.also { it.itemMeta = meta }
            }
        }
    }

    private fun Color.toBukkit(): org.bukkit.Color {
        return org.bukkit.Color.fromRGB(this.rgb)
    }
}