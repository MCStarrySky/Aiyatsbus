package com.mcstarrysky.aiyatsbus.module.command.subcommand

import com.mcstarrysky.aiyatsbus.module.ui.AnvilUI
import com.mcstarrysky.aiyatsbus.module.ui.EnchantSearchUI
import com.mcstarrysky.aiyatsbus.module.ui.ItemCheckUI
import com.mcstarrysky.aiyatsbus.module.ui.MainMenuUI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.common.platform.command.suggestPlayers
import taboolib.platform.util.sendLang

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Menu
 *
 * @author mical
 * @since 2024/3/3 17:39
 */
val menuSubCommand = subCommand {
    execute<CommandSender> { sender, _, _ -> handleMenu(sender) }
    dynamic("player", true) {
        suggestPlayers()
        execute<CommandSender> { sender, args, _ -> handleMenu(sender, args["player"]) }

        dynamic(optional = true) {
            suggest { menus }

            execute<CommandSender> { sender, args, menu ->
                handleMenu(sender, menu, args["player"])
            }
        }
    }
}

private fun handleMenu(sender: CommandSender, menu: String = "main", who: String? = null) {
    (who?.let { Bukkit.getPlayer(it) } ?: (sender as? Player))?.let { receiver ->
        when(menu) {
            "main" -> MainMenuUI.open(receiver)
            "anvil" -> AnvilUI.open(receiver)
            "search" -> EnchantSearchUI.open(receiver)
            "check" -> ItemCheckUI.open(receiver, mode = ItemCheckUI.CheckMode.FIND)
        }
    } ?: sender.sendLang("command-subCommands-menu-fail")
}

val menus = listOf("main", "anvil", "search", "check")