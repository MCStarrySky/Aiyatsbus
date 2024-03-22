package com.mcstarrysky.aiyatsbus.module.kether.action

import com.mcstarrysky.aiyatsbus.core.FurtherOperation
import com.mcstarrysky.aiyatsbus.module.kether.AiyatsbusParser
import com.mcstarrysky.aiyatsbus.module.kether.aiyatsbus
import org.bukkit.block.Block

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.kether.action.ActionFurtherOperation
 *
 * @author mical
 * @since 2024/3/21 22:51
 */
object ActionFurtherOperation {

    @AiyatsbusParser(["further-operation"])
    fun furtherOperation() = aiyatsbus {
        when (nextToken()) {
            "addOperation", "add-operation", "add" -> {
                combine(player(), text(), optional("with", then = text())) { player, name, data ->
                    FurtherOperation.addOperation(player, name, data)
                }
            }
            "removeOperation", "remove-operation", "remove" -> {
                combine(player(), text(), optional("with", then = text())) { player, name, data ->
                    FurtherOperation.removeOperation(player, name, data)
                }
            }
            "hasOperation", "has-operation", "has" -> {
                combine(player(), text(), optional("with", then = text())) { player, name, data ->
                    FurtherOperation.hasOperation(player, name, data)
                }
            }
            "furtherBreak", "further-break", "break" -> {
                combine(player(), any()) { player, block ->
                    FurtherOperation.furtherBreak(player, block as Block)
                }
            }
            else -> error("unknown operation")
        }
    }
}