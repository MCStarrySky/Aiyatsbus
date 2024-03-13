package com.mcstarrysky.aiyatsbus.module.command.subcommand

import com.mcstarrysky.aiyatsbus.core.util.Mirror
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Report
 *
 * @author mical
 * @since 2024/3/13 20:50
 */
val reportSubCommand = subCommand {
    execute<ProxyCommandSender> { sender, _, _ ->
        Mirror.report(sender)
    }
}