package com.mcstarrysky.aiyatsbus.module.ingame.command.subcommand

import com.mcstarrysky.aiyatsbus.core.AIYATSBUS_PREFIX
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.core.util.Reloadables
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.module.chat.colored
import taboolib.module.configuration.ConfigLoader
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.nmsProxy
import taboolib.platform.util.onlinePlayers

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Reload
 *
 * @author mical
 * @since 2024/3/3 19:33
 */
val reloadSubCommand = subCommand {
    execute<CommandSender> { sender, _, _ ->
        if (MinecraftVersion.majorLegacy >= 12003) {
            (Aiyatsbus.api().getEnchantmentRegisterer() as ModernEnchantmentRegisterer).replaceRegistry()
        }
        ConfigLoader.files.values.forEach { it.configuration.reload() }
        Reloadables.execute()
        onlinePlayers.forEach(Player::updateInventory)
        sender.sendMessage("$AIYATSBUS_PREFIX Done.".colored())
    }
}