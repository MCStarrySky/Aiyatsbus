package com.mcstarrysky.aiyatsbus.module.ingame.command.subcommand

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.compat.EnchantRegistrationHooks
import com.mcstarrysky.aiyatsbus.core.registration.modern.ModernEnchantmentRegisterer
import com.mcstarrysky.aiyatsbus.core.util.Reloadables
import com.mcstarrysky.aiyatsbus.module.ingame.command.AiyatsbusCommand
import com.mcstarrysky.aiyatsbus.module.ingame.mechanics.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.Language
import taboolib.module.nms.MinecraftVersion
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
        val time = System.currentTimeMillis()
        if (MinecraftVersion.majorLegacy >= 12003) {
            (Aiyatsbus.api().getEnchantmentRegisterer() as ModernEnchantmentRegisterer).replaceRegistry()
        }
        Language.reload()
        AiyatsbusSettings.conf.reload()
        Reloadables.execute()
        Aiyatsbus.api().getDisplayManager().getSettings().conf.reload()
        AnvilSupport.conf.reload()
        EnchantingTableSupport.conf.reload()
        ExpModifier.conf.reload()
        GrindstoneSupport.conf.reload()
        VillagerSupport.conf.reload()
        onlinePlayers.forEach(Player::updateInventory)
        AiyatsbusCommand.init() // 重新生成 TabList
        sender.sendLang("plugin-reload", System.currentTimeMillis() - time)
        EnchantRegistrationHooks.unregisterHooks()
        EnchantRegistrationHooks.registerHooks()
    }
}