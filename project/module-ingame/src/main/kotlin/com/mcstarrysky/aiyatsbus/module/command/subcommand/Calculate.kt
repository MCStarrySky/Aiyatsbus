package com.mcstarrysky.aiyatsbus.module.command.subcommand

import com.mcstarrysky.aiyatsbus.core.util.calculate
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Calculate
 *
 * @author mical
 * @since 2024/3/3 17:47
 */
val calculateSubCommand = subCommand {
    dynamic("expression", true) {
        execute<CommandSender> { sender, args, _ ->
            sender.sendMessage(run {
                try {
                    args["expression"].calculate()
                } catch (e: Exception) {
                    "error"
                }
            })
        }
    }
}