package com.mcstarrysky.aiyatsbus.module.kether.action.item

import com.mcstarrysky.aiyatsbus.module.kether.util.playerOrNull
import com.mcstarrysky.aiyatsbus.module.kether.util.toBukkit
import taboolib.platform.util.giveItem

/**
 * Vulpecula
 * top.lanscarlos.vulpecula.bacikal.action.item
 *
 * @author Lanscarlos
 * @since 2023-03-27 23:11
 */
object ActionItemGive : ActionItem.Resolver {

    override val name: Array<String> = arrayOf("give")

    override fun resolve(reader: ActionItem.Reader): ActionItem.Handler<out Any?> {
        return reader.transfer {
            combine(
                source(),
                optional("to", then = player()),
                optional("with", "repeat", then = int(), def = 1)
            ) { item, player, repeat ->
                val target = player ?: this.playerOrNull()?.toBukkit() ?: error("No player selected.")
                target.giveItem(item, repeat)
                item
            }
        }
    }
}