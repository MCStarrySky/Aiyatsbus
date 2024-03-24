package com.mcstarrysky.aiyatsbus.module.ingame.command.subcommand

import com.mcstarrysky.aiyatsbus.core.book
import com.mcstarrysky.aiyatsbus.core.data.Rarity
import com.mcstarrysky.aiyatsbus.core.drawEt
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.command.suggestPlayers
import taboolib.module.kether.isInt
import taboolib.platform.util.giveItem
import taboolib.platform.util.sendLang

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.subcommand.Random
 *
 * @author mical
 * @since 2024/3/3 19:04
 */
val randomSubCommand = subCommand {
    dynamic("rarity") {
        suggestion<CommandSender> { _, _ -> Rarity.rarities.map { it.key } + Rarity.rarities.map { it.value.name } }
        execute<CommandSender> { sender, args, _ -> handleRandom(sender, null, Rarity.getRarity(args["rarity"])!!) }
        dynamic("level", true) {
            suggestionUncheck<CommandSender> { _, _ -> listOf("等级") }
            execute<CommandSender> { sender, args, _ -> handleRandom(sender, null, Rarity.getRarity(args["rarity"])!!, args["level"]) }
            dynamic("player", true) {
                suggestPlayers()
                execute<CommandSender> { sender, args, _ -> handleRandom(sender, args["player"], Rarity.getRarity(args["rarity"])!!, args["level"]) }
            }
        }
    }
}

private fun handleRandom(sender: CommandSender, who: String?, rarity: Rarity, level: String? = "100") {
    val enchant = rarity.drawEt() ?: run {
        sender.sendLang("command-subCommands-random-rarity")
        return
    }
    if (level?.isInt() != true) {
        sender.sendLang("command-subCommands-random-number")
        return
    }
    val lv = level.toInt().coerceAtLeast(1).coerceAtMost(enchant.basicData.maxLevel)
    (who?.let { Bukkit.getPlayer(it) } ?: (sender as? Player))?.let { receiver ->
        receiver.giveItem(enchant.book(lv))
        sender.sendLang("command-subCommands-random-sender", receiver.name to "name", enchant.displayName(lv) to "enchantment")
        receiver.sendLang("command-subCommands-random-receiver", enchant.displayName(lv) to "enchantment")
    } ?: sender.sendLang("command-subCommands-random-fail")
}