package com.mcstarrysky.aiyatsbus.module.ingame.command

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.module.ingame.command.subcommand.*
import com.mcstarrysky.aiyatsbus.module.ingame.ui.internal.function.variable
import org.bukkit.command.CommandSender
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.command.*
import taboolib.common.platform.command.component.CommandBase
import taboolib.common.platform.command.component.CommandComponent
import taboolib.common.platform.command.component.CommandComponentLiteral
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.registerLifeCycleTask
import taboolib.common.util.Strings
import taboolib.module.chat.component
import taboolib.module.nms.MinecraftVersion

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.command.AiyatsbusCommand
 *
 * @author mical
 * @since 2024/2/17 21:02
 */
@CommandHeader(name = "aiyatsbus", permission = "aiyatsbus.command", aliases = ["se", "spe", "splendidenchants", "nereus", "nos", "nereusopus"])
object AiyatsbusCommand {

    val enchantNamesAndIds = mutableListOf<String>()

    @CommandBody(permission = "aiyatsbus.command")
    val main = mainCommand {
        createTabooLegacyHelper()
    }

    @CommandBody(permission = "aiyatsbus.command.book")
    val book = bookSubCommand

    @CommandBody(permission = "aiyatsbus.command.enchant")
    val enchant = enchantSubCommand

    @CommandBody(permission = "aiyatsbus.command.menu")
    val menu = menuSubCommand

    @CommandBody(permission = "aiyatsbus.command.mode")
    val mode = modeSubCommand

    @CommandBody(permission = "aiyatsbus.command.random")
    val random = randomSubCommand

    @CommandBody(permission = "aiyatsbus.command.reload")
    val reload = reloadSubCommand

    @CommandBody(permission = "aiyatsbus.command.report")
    val report = reportSubCommand

    @Awake(LifeCycle.INIT)
    fun init() {
        registerLifeCycleTask(LifeCycle.ACTIVE, 999) {
            // 生成 TabList
            enchantNamesAndIds.clear()
            enchantNamesAndIds.addAll(Aiyatsbus.api().getEnchantmentManager().getByIDs().keys + Aiyatsbus.api().getEnchantmentManager().getByNames().keys)
        }
    }
}

fun CommandComponent.createTabooLegacyHelper() {
    execute<CommandSender> { sender, _, _ ->
        val text = mutableListOf<String>()

        for (command in children.filterIsInstance<CommandComponentLiteral>()) {
            if (!sender.isOp) {
                if (!sender.hasPermission(command.permission)) continue
                else if (command.hidden) continue
            }
            val name = command.aliases[0]
            var usage = sender.asLangOrNull("command-subCommands-$name-usage") ?: ""
            if (usage.isNotEmpty()) usage += " "
            val description = sender.asLangOrNull("command-subCommands-$name-description") ?: sender.asLang("command-no-desc")
            text += sender.asLangList("command-sub", name to "name", description to "description", usage to "usage")
        }

        sender.asLangList(
            "command-helper",
            pluginVersion to "pluginVersion",
            MinecraftVersion.minecraftVersion to "minecraftVersion"
        ).variable("subCommands", text).forEach { it.component().buildColored().sendTo(adaptCommandSender(sender)) }
    }

    if (this is CommandBase) {
        incorrectCommand { s, ctx, _, state ->
            val sender = s.cast<CommandSender>()
            val input = ctx.args().first()
            val name = children.filterIsInstance<CommandComponentLiteral>()
                .firstOrNull { it.aliases.contains(input) }?.aliases?.get(0) ?: input
            var usage = sender.asLangOrNull("command-subCommands-$name-usage") ?: ""
            if (usage.isNotEmpty()) usage += " "
            val description = sender.asLangOrNull("command-subCommands-$name-description") ?: sender.asLang("command-no-desc")

            when (state) {
                // 缺参数
                1 -> {
                    sender.sendLang("command-argument-missing", name to "name", usage to "usage", description to "description")
                }

                // 参数错误
                2 -> {
                    if (ctx.args().size > 1) {
                        sender.sendLang("command-argument-wrong", name to "name", usage to "usage", description to "description")
                    } else {
                        val similar = children.filterIsInstance<CommandComponentLiteral>()
                            .filterNot { it.hidden }
                            .filter { sender.hasPermission(it.permission) }
                            .maxByOrNull { Strings.similarDegree(name, it.aliases[0]) }!!
                            .aliases[0]
                        sender.sendLang("command-argument-unknown", name to "name", similar to "similar")
                    }
                }
            }
        }

        incorrectSender { sender, ctx ->
            (sender.cast<CommandSender>()).sendLang("command-incorrect-sender", ctx.args().first() to "name")
        }
    }
}