package com.mcstarrysky.aiyatsbus.module.command.subcommand

import com.mcstarrysky.aiyatsbus.core.data.MenuMode
import com.mcstarrysky.aiyatsbus.core.menuMode
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggestPlayers
import taboolib.platform.util.sendLang

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Mode
 *
 * @author mical
 * @since 2024/3/3 18:59
 */
val modeSubCommand = subCommand {
    dynamic("mode", true) {
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