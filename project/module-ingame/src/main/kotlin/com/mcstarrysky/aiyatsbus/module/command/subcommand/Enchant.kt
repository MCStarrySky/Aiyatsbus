package com.mcstarrysky.aiyatsbus.module.command.subcommand

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.addEt
import com.mcstarrysky.aiyatsbus.core.aiyatsbusEt
import com.mcstarrysky.aiyatsbus.core.removeEt
import com.mcstarrysky.aiyatsbus.core.util.isNull
import com.mcstarrysky.aiyatsbus.module.command.AiyatsbusCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggestPlayers
import taboolib.platform.util.asLangText
import taboolib.platform.util.sendLang

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Enchant
 *
 * @author mical
 * @since 2024/3/3 17:48
 */
val enchantSubCommand = subCommand {
    dynamic("enchant") {
        suggestion<CommandSender> { _, _ -> AiyatsbusCommand.enchantNamesAndIds }
        execute<CommandSender> { sender, args, _ -> handleEnchant(sender, null, aiyatsbusEt(args["enchant"])!!) }
        dynamic("level", true) {
            suggestion<Player> level@{ _, cmd ->
                val maxLevel = (aiyatsbusEt(cmd["enchant"])?.basicData?.maxLevel ?: return@level listOf())
                buildList { repeat(maxLevel + 1) { add("$it") } }
            }
            execute<CommandSender> { sender, args, _ -> handleEnchant(sender, null, aiyatsbusEt(args["enchant"])!!, args["level"].toInt()) }
            dynamic("player", true) {
                suggestPlayers()
                execute<CommandSender> { sender, args, _ -> handleEnchant(sender, args["player"], aiyatsbusEt(args["enchant"])!!, args["level"].toInt()) }
            }
        }
    }
}

private fun handleEnchant(sender: CommandSender, who: String?, enchant: AiyatsbusEnchantment, level: Int = enchant.basicData.maxLevel) {
    (who?.let { Bukkit.getPlayer(it) } ?: (sender as? Player))?.let { receiver ->
        val item = receiver.inventory.itemInMainHand
        if (item.isNull) {
            sender.sendLang("command-subCommands-enchant-empty")
            return
        }
        val state = if (level == 0) sender.asLangText("remove") else sender.asLangText("add")
        if (level == 0) item.removeEt(enchant)
        else item.addEt(enchant, level)
        sender.sendLang(
            "command-subCommands-enchant-sender",
            receiver.name to "name",
            state to "state",
            enchant.displayName(level) to "enchantment"
        )
        receiver.sendLang("command-subCommands-enchant-receiver", state to "state", enchant.displayName(level) to "enchantment")
    } ?: sender.sendLang("command-subCommands-enchant-fail")
}