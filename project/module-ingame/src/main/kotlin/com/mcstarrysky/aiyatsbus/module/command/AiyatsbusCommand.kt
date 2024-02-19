package com.mcstarrysky.aiyatsbus.module.command

import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.addEt
import com.mcstarrysky.aiyatsbus.core.aiyatsbusEt
import com.mcstarrysky.aiyatsbus.module.ui.MainMenuUI
import org.bukkit.entity.Player
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.AiyatsbusCommand
 *
 * @author mical
 * @since 2024/2/17 21:02
 */
@CommandHeader(name = "aiyatsbus", permission = "aiyatsbus.command")
object AiyatsbusCommand {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val devByID = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            Aiyatsbus.api().getEnchantmentManager().getByIDs().keys.forEach(sender::sendMessage)
        }
    }

    @CommandBody
    val devEnchant = subCommand {
        execute<Player> { sender, _, _ ->
            sender.equipment.itemInMainHand.addEt(aiyatsbusEt("test") ?: return@execute)
        }
    }

    @CommandBody
    val devMenu = subCommand {
        execute<Player> { sender, _, _ ->
            MainMenuUI.open(sender)
        }
    }
}