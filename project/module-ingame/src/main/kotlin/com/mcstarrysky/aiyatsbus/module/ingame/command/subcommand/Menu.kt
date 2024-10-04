/*
 *  Copyright (C) 2022-2024 SummerIceBearStudio
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.mcstarrysky.aiyatsbus.module.ingame.command.subcommand

import com.mcstarrysky.aiyatsbus.core.sendLang
import com.mcstarrysky.aiyatsbus.module.ingame.ui.AnvilUI
import com.mcstarrysky.aiyatsbus.module.ingame.ui.EnchantSearchUI
import com.mcstarrysky.aiyatsbus.module.ingame.ui.ItemCheckUI
import com.mcstarrysky.aiyatsbus.module.ingame.ui.MainMenuUI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggest
import taboolib.common.platform.command.suggestPlayers

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Menu
 *
 * @author mical
 * @since 2024/3/3 17:39
 */
val menuSubCommand = subCommand {
    execute<CommandSender> { sender, _, _ -> handleMenu(sender) }
    dynamic("player", true, permission = "aiyatsbus.command.menu.other") {
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