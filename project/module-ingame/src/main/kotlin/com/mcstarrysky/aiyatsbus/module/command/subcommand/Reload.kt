package com.mcstarrysky.aiyatsbus.module.command.subcommand

import com.mcstarrysky.aiyatsbus.core.AIYATSBUS_PREFIX
import com.mcstarrysky.aiyatsbus.core.AiyatsbusDisplayManager
import com.mcstarrysky.aiyatsbus.core.AiyatsbusSettings
import com.mcstarrysky.aiyatsbus.core.mechanism.Reloadables
import com.mcstarrysky.aiyatsbus.module.listener.mechanism.*
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.module.chat.colored
import taboolib.module.configuration.ConfigLoader

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Reload
 *
 * @author mical
 * @since 2024/3/3 19:33
 */
val reloadSubCommand = subCommand {
    execute<CommandSender> { sender, _, _ ->
        // AiyatsbusSettings.conf.reload()
        // AiyatsbusDisplayManager.conf.reload()
        // AnvilListener.conf.reload()
        // AttainListener.conf.reload()
        // ExpListener.conf.reload()
        // GrindstoneListener.conf.reload()
        // VillagerListener.conf.reload()
        ConfigLoader.files.values.forEach { it.configuration.reload() }
        Reloadables.execute()
        sender.sendMessage("$AIYATSBUS_PREFIX Done.".colored())
    }
}