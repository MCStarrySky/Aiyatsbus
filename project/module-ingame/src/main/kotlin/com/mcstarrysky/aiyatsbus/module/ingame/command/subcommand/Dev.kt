package com.mcstarrysky.aiyatsbus.module.ingame.command.subcommand

import com.mcstarrysky.aiyatsbus.core.toDisplayMode
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.giveItem

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.ingame.command.subcommand.Dev
 *
 * @author mical
 * @date 2024/9/7 13:10
 */
val devSubCommand = subCommand {
    execute<Player> { sender, _, _ ->
        sender.giveItem(sender.equipment.itemInMainHand.toDisplayMode(sender))
    }
}