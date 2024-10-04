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

import com.mcstarrysky.aiyatsbus.core.data.MenuMode
import com.mcstarrysky.aiyatsbus.core.menuMode
import com.mcstarrysky.aiyatsbus.core.sendLang
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggestPlayers

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Mode
 *
 * @author mical
 * @since 2024/3/3 18:59
 */
val modeSubCommand = subCommand {
    dynamic("mode") {
        suggestion<CommandSender> { _, _ -> listOf("作弊", "普通", "cheat", "normal") }
        execute<CommandSender> { sender, args, _ -> handleMode(sender, args["mode"], sender.name) }
        dynamic("player", true) {
            suggestPlayers()
            execute<CommandSender> { sender, args, _ -> handleMode(sender, args["mode"], args["player"]) }
        }
    }
}

private fun handleMode(sender: CommandSender, mode: String, who: String?) {
    (who?.let { Bukkit.getPlayer(it) } ?: (sender as? Player))?.let { receiver ->
        receiver.menuMode = when (mode) {
            "作弊", "cheat" -> MenuMode.CHEAT
            "普通", "normal" -> MenuMode.NORMAL
            else -> MenuMode.NORMAL
        }
        receiver.sendLang("command-subCommands-mode-receiver", mode to "mode")
        sender.sendLang("command-subCommands-mode-sender", mode to "mode", receiver.name to "name")
    } ?: sender.sendLang("command-subCommands-mode-fail")
}